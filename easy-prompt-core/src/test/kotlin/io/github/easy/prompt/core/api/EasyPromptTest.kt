package io.github.easy.prompt.core.api

import io.github.easy.prompt.core.api.model.template.HistoryChats
import io.github.easy.prompt.core.api.model.template.PromptParams
import io.github.easy.prompt.core.infrastructure.provider.LLMProviders
import io.github.easy.prompt.core.infrastructure.provider.vendors.openrouter.OpenrouterLLMClient
import io.github.easy.prompt.core.infrastructure.tools.propread.PropertiesUtils
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class EasyPromptTest {

    @Test
    fun invokePrompt() {

        val prop = PropertiesUtils.fromClasspath("app.local.properties")

        val easyPrompt = EasyPrompt(
            llmProviders = LLMProviders(
                llmClients = mutableListOf(
                    OpenrouterLLMClient(
                        apiUrl = prop.getProperty("openrouter.api.url", ""),
                        apiKey = prop.getProperty("openrouter.api.key", ""),
                    )
                )
            )
        )


        val historyChats = easyPrompt.invokePrompt(
            "prompts/translate_prompt.yaml",
            PromptParams().addParams(
                "content",
                "My only vehicle I have right now is a 2021 RAM 2500 diesel. I wouldgladly trade it in for a Cybertruck just for the turning radius alone."
            ),
            HistoryChats()
        )

        assertNotNull(historyChats)

    }

}