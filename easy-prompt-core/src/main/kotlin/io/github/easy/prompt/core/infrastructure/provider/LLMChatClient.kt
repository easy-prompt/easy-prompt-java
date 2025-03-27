package io.github.easy.prompt.core.infrastructure.provider

import io.github.easy.prompt.core.api.model.ChatCompletion
import io.github.easy.prompt.core.api.model.HistoryChats

class LLMChatClient : ILLMClient {

    override fun invoke(
        systemPrompts: List<String>,
        fullPrompt: String,
        historyChats: HistoryChats,
        modelName: String
    ): ChatCompletion {
        TODO("Not yet implemented")
    }

}