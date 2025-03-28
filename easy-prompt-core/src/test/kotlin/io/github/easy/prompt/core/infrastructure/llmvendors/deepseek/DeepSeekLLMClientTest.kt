package io.github.easy.prompt.core.infrastructure.llmvendors.deepseek

import io.github.easy.prompt.core.api.model.llmclient.PromptInvokeParam
import io.github.easy.prompt.core.infrastructure.utils.propread.PropertiesUtils
import org.junit.jupiter.api.Test

class DeepSeekLLMClientTest {

    @Test
    fun `test invoke`() {

        val prop = PropertiesUtils.fromClasspath("app.local.properties")

        val llmClient = DeepSeekLLMClient(
            apiKey = prop.getProperty("deepseek.api.key", "")
        )

        val completion = llmClient.invoke(
            fullPrompt = "1+1等于几？",
            promptInvokeParam = PromptInvokeParam(
                model = "deepseek-reasoner"
            )
        )

        println(completion)

    }

}