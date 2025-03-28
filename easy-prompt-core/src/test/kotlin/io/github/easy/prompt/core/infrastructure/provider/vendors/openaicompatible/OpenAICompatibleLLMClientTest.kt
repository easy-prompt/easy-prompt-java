package io.github.easy.prompt.core.infrastructure.provider.vendors.openaicompatible

import io.github.easy.prompt.core.api.model.llmclient.PromptInvokeParam
import io.github.easy.prompt.core.infrastructure.llmvendors.openaicompatible.OpenAICompatibleLLMClient
import io.github.easy.prompt.core.infrastructure.utils.propread.PropertiesUtils
import org.junit.jupiter.api.Test

class OpenAICompatibleLLMClientTest {

    @Test
    fun `test invoke`() {

        val prop = PropertiesUtils.fromClasspath("app.local.properties")

        val llmClient = OpenAICompatibleLLMClient(
            apiKey = prop.getProperty("openrouter.api.key", ""),
            apiUrl = prop.getProperty("openrouter.api.url", ""),
        )

        val completion = llmClient.invoke(
            fullPrompt = "My only vehicle I have right now is a 2021 RAM 2500 diesel. I wouldgladly trade it in for a Cybertruck just for the turning radius alone.",
            promptInvokeParam = PromptInvokeParam(
                model = "deepseek/deepseek-chat-v3-0324"
            )
        )

        println(completion)

    }

}