package io.github.easy.prompt.core.infrastructure.provider.vendors.openaicompatible

import io.github.easy.prompt.core.api.model.invoke.PromptInvokeParam
import io.github.easy.prompt.core.infrastructure.tools.propread.PropertiesUtils
import org.junit.jupiter.api.Test

class OpenAICompatibleLLMClientTest {

    @Test
    fun `test invoke`() {

        val prop = PropertiesUtils.fromClasspath("app.local.properties")

        val llmClient = OpenAICompatibleLLMClient(
            apiUrl = prop.getProperty("openrouter.api.url", ""),
            apiKey = prop.getProperty("openrouter.api.key", ""),
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