package io.github.easy.prompt.core.api.model.tools

import io.github.easy.prompt.core.api.model.llmclient.EmptyStreamingHandler
import io.github.easy.prompt.core.api.model.llmclient.PromptInvokeParam
import io.github.easy.prompt.core.api.model.llmclient.StreamingHandler
import io.github.easy.prompt.core.api.model.template.ChatCompletion
import io.github.easy.prompt.core.api.model.template.HistoryChats

interface IToolsClient {

    /**
     * Sync invoke LLM chat api
     *
     * @param systemPrompts system prompt
     * @param fullPrompt full prompt
     * @param historyChats history chats
     * @return ChatCompletion
     */
    fun invoke(
        systemPrompts: List<String> = emptyList(), fullPrompt: String,
        historyChats: HistoryChats = HistoryChats(),
        promptInvokeParam: PromptInvokeParam,
        streamingHandler: StreamingHandler = EmptyStreamingHandler()
    ): ChatCompletion

    /**
     * @return the provider name like openai, openrouter, azure, etc.
     */
    fun toolsName(): String

}