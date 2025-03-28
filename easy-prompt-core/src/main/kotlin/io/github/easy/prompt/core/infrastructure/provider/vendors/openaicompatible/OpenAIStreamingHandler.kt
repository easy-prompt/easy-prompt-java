package io.github.easy.prompt.core.infrastructure.provider.vendors.openaicompatible

import com.openai.core.http.AsyncStreamResponse.Handler
import com.openai.models.chat.completions.ChatCompletionChunk
import io.github.easy.prompt.core.infrastructure.provider.StreamingHandler

class OpenAIStreamingHandler(
    private val streamingHandler: StreamingHandler
) : Handler<ChatCompletionChunk> {

    override fun onNext(value: ChatCompletionChunk) {
        streamingHandler.onNext(value)
    }

}