package io.github.easy.prompt.core.api

import io.github.easy.prompt.core.api.model.HistoryChats
import io.github.easy.prompt.core.api.model.PromptParams

/**
 * LLM client interface
 */
interface ILLMClient {

    /**
     * Sync invoke LLM chat api
     */
    fun invoke(modelName: String, promptParams: PromptParams, historyChats: HistoryChats)

    /**
     * Async invoke LLM chat api
     */
    fun invokeAsync(modelName: String, promptParams: PromptParams, historyChats: HistoryChats)

    /**
     * supported model name
     */
    fun supportedModelName(): String

}