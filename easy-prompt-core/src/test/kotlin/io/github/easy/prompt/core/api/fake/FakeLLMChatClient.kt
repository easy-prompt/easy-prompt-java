package io.github.easy.prompt.core.api.fake

import io.github.easy.prompt.core.infrastructure.provider.ILLMClient
import io.github.easy.prompt.core.api.model.ChatCompletion
import io.github.easy.prompt.core.api.model.HistoryChats

class FakeLLMChatClient : ILLMClient {

    override fun invoke(
        systemPrompts: List<String>, fullPrompt: String,
        historyChats: HistoryChats, modelName: String
    ): ChatCompletion {

        return ChatCompletion(
            chatModel = modelName, prompt = fullPrompt,
            answer = "fake answer", reasoning = "fake reasoning", images = emptyList()
        )

    }

}