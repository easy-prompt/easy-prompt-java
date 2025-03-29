package io.github.easy.prompt.core.infrastructure.llmvendors.openaicompatible

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam
import com.openai.models.chat.completions.ChatCompletionCreateParams
import io.github.easy.prompt.core.api.model.llmclient.ILLMClient
import io.github.easy.prompt.core.api.model.llmclient.PromptInvokeParam
import io.github.easy.prompt.core.api.model.llmclient.StreamingHandler
import io.github.easy.prompt.core.api.model.template.ChatCompletion
import io.github.easy.prompt.core.api.model.template.HistoryChats
import java.util.concurrent.Executors

class OpenAICompatibleLLMClient(
    private val apiUrl: String,
    private val apiKey: String,
    private val openaiClient: OpenAIClient = OpenAIOkHttpClient.builder()
        .apiKey(apiKey)
        .baseUrl(apiUrl)
        .streamHandlerExecutor(Executors.newFixedThreadPool(10))
        .build(),
    private val reasoningFieldName: String = "reasoning_content"
) : ILLMClient {

    override fun invoke(
        systemPrompts: List<String>, fullPrompt: String, historyChats: HistoryChats,
        promptInvokeParam: PromptInvokeParam, streamingHandler: StreamingHandler
    ): ChatCompletion {

        val params = prepareChatParams(systemPrompts, historyChats, fullPrompt, promptInvokeParam)

        val chatCompletionCache = ChatCompletionCache()

        var reasoningMark = ReasoningMark.CHAT_INIT

        openaiClient.chat().completions().createStreaming(params).use { streamResponse ->

            streamResponse.stream()
                .flatMap { completion -> completion.choices().stream() }
                .filter { choice ->
                    choice.delta()
                        ._additionalProperties()[reasoningFieldName]?.asString()?.isPresent == true
                            || choice.delta().content().isPresent
                }
                .forEach { choice ->

                    // 如果是推理模型，输出推理过程之前添加 <think> 标签
                    if (choice.delta()._additionalProperties()[reasoningFieldName]?.asString()?.isPresent == true
                        && choice.delta().content().map { it.isEmpty() }.orElse(false)
                        && chatCompletionCache.reasoningStringCache.isEmpty()
                        && reasoningMark == ReasoningMark.CHAT_INIT
                    ) {
                        streamingHandler.onNext("<think>")
                        reasoningMark = ReasoningMark.REASONING
                    }

                    choice.delta()
                        ._additionalProperties()[reasoningFieldName]
                        ?.asString()
                        ?.ifPresent {
                            chatCompletionCache.reasoningStringCache.append(it)
                            streamingHandler.onNext(it)
                        }

                    // 如果是推理模型，输出推理过程之后添加 </think> 标签
                    if (choice.delta().content().map { it.isNotBlank() }.orElse(false)
                        && chatCompletionCache.answerStringCache.isEmpty()
                        && reasoningMark == ReasoningMark.REASONING
                    ) {
                        streamingHandler.onNext("</think>")
                        reasoningMark = ReasoningMark.ANSWER
                    }

                    choice
                        .delta()
                        .content()
                        .ifPresent {
                            chatCompletionCache.answerStringCache.append(it)
                            streamingHandler.onNext(it)
                        }

                }

        }

        // 获取完整的聊天完成结果
        return ChatCompletion(
            chatModel = promptInvokeParam.model,
            prompt = fullPrompt,
            answer = chatCompletionCache.answerStringCache.toString(),
            reasoning = chatCompletionCache.reasoningStringCache.toString(),
        )

    }

    private fun prepareChatParams(
        systemPrompts: List<String>,
        historyChats: HistoryChats, fullPrompt: String,
        promptInvokeParam: PromptInvokeParam
    ): ChatCompletionCreateParams {

        val paramBuilder = ChatCompletionCreateParams.builder()

        systemPrompts.forEach { paramBuilder.addSystemMessage(it) }

        historyChats.historyChats.forEach { chat ->
            paramBuilder
                .addUserMessage(chat.prompt)
                .addMessage(
                    ChatCompletionAssistantMessageParam.builder().content(chat.answer ?: "").build()
                )
        }

        paramBuilder.addUserMessage(fullPrompt)
        promptInvokeParam.maxTokens?.let { paramBuilder.maxCompletionTokens(it.toLong()) }
        promptInvokeParam.temperature.let { paramBuilder.temperature(it) }
        promptInvokeParam.topP?.let { paramBuilder.topP(it) }

        paramBuilder.model(promptInvokeParam.model)

        return paramBuilder.build()

    }

    override fun providerName(): String {
        return "OpenAICompatible"
    }


}

class ChatCompletionCache(
    val reasoningStringCache: StringBuilder = StringBuilder(),
    val answerStringCache: StringBuilder = StringBuilder()
)

enum class ReasoningMark {
    CHAT_INIT,
    REASONING,
    ANSWER
}
