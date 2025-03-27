package io.github.easy.prompt.core.infrastructure.provider

import io.github.easy.prompt.core.api.model.ChatCompletion
import io.github.easy.prompt.core.api.model.HistoryChats

/**
 * LLM client interface
 */
interface ILLMClient {

    /**
     * Sync invoke LLM chat api
     *
     * @param systemPrompts system prompt
     * @param fullPrompt full prompt
     * @param historyChats history chats
     * @return ChatCompletion
     */
    fun invoke(systemPrompts: List<String>, fullPrompt: String, historyChats: HistoryChats): ChatCompletion

}