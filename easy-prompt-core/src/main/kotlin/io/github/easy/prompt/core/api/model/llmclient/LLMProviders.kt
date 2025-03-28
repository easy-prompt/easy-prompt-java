package io.github.easy.prompt.core.api.model.llmclient

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