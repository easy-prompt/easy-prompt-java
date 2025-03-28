package io.github.easy.prompt.core.infrastructure.provider.vendors.openrouter

import io.github.easy.prompt.core.api.model.invoke.PromptInvokeParam
import io.github.easy.prompt.core.api.model.template.ChatCompletion
import io.github.easy.prompt.core.api.model.template.HistoryChats
import io.github.easy.prompt.core.infrastructure.provider.ILLMClient
import io.github.easy.prompt.core.infrastructure.provider.StreamingHandler
import io.github.easy.prompt.core.infrastructure.provider.vendors.openaicompatible.OpenAICompatibleLLMClient

class OpenrouterLLMClient(
    private val apiUrl: String,
    private val apiKey: String,
    private val openrouterClient: OpenAICompatibleLLMClient = OpenAICompatibleLLMClient(apiUrl, apiKey)
) : ILLMClient {

    override fun invoke(
        systemPrompts: List<String>,
        fullPrompt: String,
        historyChats: HistoryChats,
        promptInvokeParam: PromptInvokeParam,
        streamingHandler: StreamingHandler
    ): ChatCompletion {

        return openrouterClient.invoke(systemPrompts, fullPrompt, historyChats, promptInvokeParam, streamingHandler)
    }

    override fun providerName(): String {
        return "openrouter"
    }
}