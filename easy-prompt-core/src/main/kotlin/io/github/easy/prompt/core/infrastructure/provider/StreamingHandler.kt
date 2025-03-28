package io.github.easy.prompt.core.infrastructure.provider

interface StreamingHandler {

    fun onNext(data: Any);

    fun onComplete(data: Any);

}

class EmptyStreamingHandler : StreamingHandler {

    override fun onNext(data: Any) {
        // do nothing
        println("onNext: $data")
    }

    override fun onComplete(data: Any) {
        // do nothing
        println("onNext: $data")
    }

}
