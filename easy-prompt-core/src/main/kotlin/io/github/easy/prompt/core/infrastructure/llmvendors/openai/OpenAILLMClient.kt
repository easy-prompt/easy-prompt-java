package io.github.easy.prompt.core.infrastructure.llmvendors.openai

import io.github.easy.prompt.core.api.model.llmclient.PromptInvokeParam
import io.github.easy.prompt.core.api.model.template.ChatCompletion
import io.github.easy.prompt.core.api.model.template.HistoryChats
import io.github.easy.prompt.core.api.model.llmclient.ILLMClient
import io.github.easy.prompt.core.api.model.llmclient.StreamingHandler
import io.github.easy.prompt.core.infrastructure.llmvendors.openaicompatible.OpenAICompatibleLLMClient

class OpenAILLMClient(
    private val apiKey: String,
    private val apiUrl: String,
    private val openAICompatibleLLMClient: OpenAICompatibleLLMClient = OpenAICompatibleLLMClient(apiUrl, apiKey)
) : ILLMClient {

    override fun invoke(
        systemPrompts: List<String>,
        fullPrompt: String,
        historyChats: HistoryChats,
        promptInvokeParam: PromptInvokeParam,
        streamingHandler: StreamingHandler
    ): ChatCompletion {

        return openAICompatibleLLMClient.invoke(systemPrompts, fullPrompt, historyChats, promptInvokeParam, streamingHandler)
    }

    override fun providerName(): String {
        return "openai"
    }
}