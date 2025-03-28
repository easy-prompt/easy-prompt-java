package io.github.easy.prompt.core.infrastructure.provider.vendors.openaicompatible

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.openai.helpers.ChatCompletionAccumulator
import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam
import com.openai.models.chat.completions.ChatCompletionCreateParams
import io.github.easy.prompt.core.api.model.invoke.PromptInvokeParam
import io.github.easy.prompt.core.api.model.template.ChatCompletion
import io.github.easy.prompt.core.api.model.template.HistoryChats
import io.github.easy.prompt.core.infrastructure.provider.ILLMClient
import io.github.easy.prompt.core.infrastructure.provider.StreamingHandler
import java.util.concurrent.Executors
import kotlin.jvm.optionals.asSequence

class OpenAICompatibleLLMClient(
    private val apiUrl: String,
    private val apiKey: String,
    private val openaiClient: OpenAIClient = OpenAIOkHttpClient.builder()
        .apiKey(apiKey)
        .baseUrl(apiUrl)
        .streamHandlerExecutor(Executors.newFixedThreadPool(10))
        .build()
) : ILLMClient {

    override fun invoke(
        systemPrompts: List<String>, fullPrompt: String, historyChats: HistoryChats,
        promptInvokeParam: PromptInvokeParam, streamingHandler: StreamingHandler
    ): ChatCompletion {

        val params = prepareChatParams(historyChats, fullPrompt, promptInvokeParam)

        val chatCompletionAccumulator = ChatCompletionAccumulator.create()

        openaiClient
            .async().chat().completions().createStreaming(params)
            .subscribe({ chunk ->
                chatCompletionAccumulator.accumulate(chunk)
                    .choices().asSequence().flatMap { choice ->
                        choice.delta().content().asSequence()
                    }
                    .forEach { streamingHandler.onNext(it) }
            })
            .onCompleteFuture()
            .whenComplete { t, u -> }
            .join()

        // 获取完整的聊天完成结果
        val chatCompletion = chatCompletionAccumulator.chatCompletion()

        return ChatCompletion(
            chatModel = promptInvokeParam.model,
            prompt = fullPrompt,
            answer = chatCompletion.choices().first().message().content().get(),
            reasoning = chatCompletion.choices().first().message().content().get()
        )

    }

    private fun prepareChatParams(
        historyChats: HistoryChats, fullPrompt: String,
        promptInvokeParam: PromptInvokeParam
    ): ChatCompletionCreateParams {

        val paramBuilder = ChatCompletionCreateParams.builder()

        historyChats.historyChats.forEach { chat ->

            paramBuilder
                .addUserMessage(chat.prompt)
                .addMessage(
                    ChatCompletionAssistantMessageParam
                        .builder().content(fullPrompt).build()
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