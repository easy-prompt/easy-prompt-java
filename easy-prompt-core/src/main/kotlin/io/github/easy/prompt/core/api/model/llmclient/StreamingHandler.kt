package io.github.easy.prompt.core.api.model.llmclient

interface StreamingHandler {

    fun onNext(data: Any);

    fun onComplete();

}

class EmptyStreamingHandler : StreamingHandler {

    override fun onNext(data: Any) {
        // do nothing
        println("onNext: $data")
    }

    override fun onComplete() {
        // do nothing
        println("onComplete.")
    }

}
