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
            reasoningFieldName = "reasoning"
        )

        val completion = llmClient.invoke(
            fullPrompt = "How many Rs are there in the word 'strawberry'?",
            promptInvokeParam = PromptInvokeParam(
                model = "deepseek/deepseek-r1-distill-llama-8b"
            )
        )

        println(completion)

    }

}