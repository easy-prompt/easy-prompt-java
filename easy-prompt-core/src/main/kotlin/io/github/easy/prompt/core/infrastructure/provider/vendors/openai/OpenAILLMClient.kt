package io.github.easy.prompt.core.infrastructure.provider.vendors.openai

import io.github.easy.prompt.core.api.model.invoke.PromptInvokeParam
import io.github.easy.prompt.core.api.model.template.ChatCompletion
import io.github.easy.prompt.core.api.model.template.HistoryChats
import io.github.easy.prompt.core.infrastructure.provider.ILLMClient
import io.github.easy.prompt.core.infrastructure.provider.StreamingHandler
import io.github.easy.prompt.core.infrastructure.provider.vendors.openaicompatible.OpenAICompatibleLLMClient

class OpenAILLMClient(
    private val apiUrl: String,
    private val apiKey: String,
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