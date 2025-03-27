package io.github.easy.prompt.core.infrastructure.provider

/**
 * LLMProviders
 */
class LLMProviders(
    val llmClients: MutableList<ILLMClient> = mutableListOf()
) {

    fun acquireLLMClient(modelName: String): ILLMClient {
        TODO()
    }

}