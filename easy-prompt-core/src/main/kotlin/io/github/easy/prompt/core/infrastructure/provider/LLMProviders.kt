package io.github.easy.prompt.core.infrastructure.provider

/**
 * LLMProviders
 */
class LLMProviders(
    val llmClients: MutableList<ILLMClient> = mutableListOf()
) {

    fun acquireLLMClient(providerName: String): ILLMClient {

        return llmClients.first { it.providerName() == providerName }

    }

}