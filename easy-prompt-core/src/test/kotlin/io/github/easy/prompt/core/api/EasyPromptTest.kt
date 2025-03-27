package io.github.easy.prompt.core.api

import io.github.easy.prompt.core.api.fake.FakeLLMChatClient
import io.github.easy.prompt.core.api.model.HistoryChats
import io.github.easy.prompt.core.api.model.PromptParams
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class EasyPromptTest {

    @Test
    fun invokePrompt() {

        val easyPrompt = EasyPrompt(FakeLLMChatClient())

        val historyChats = easyPrompt.invokePrompt(
            "prompts/translate_prompt.yaml",
            PromptParams().addParams("content", "EasyPrompt"),
            HistoryChats()
        )

        assertNotNull(historyChats)

    }

}