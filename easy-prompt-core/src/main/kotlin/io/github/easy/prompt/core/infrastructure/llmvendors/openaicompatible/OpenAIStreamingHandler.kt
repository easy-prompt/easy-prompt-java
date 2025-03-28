package io.github.easy.prompt.core.infrastructure.llmvendors.openaicompatible

import com.openai.core.http.AsyncStreamResponse.Handler
import com.openai.models.chat.completions.ChatCompletionChunk
import io.github.easy.prompt.core.api.model.llmclient.StreamingHandler

class OpenAIStreamingHandler(
    private val streamingHandler: StreamingHandler
) : Handler<ChatCompletionChunk> {

    override fun onNext(value: ChatCompletionChunk) {
        streamingHandler.onNext(value)
    }

}