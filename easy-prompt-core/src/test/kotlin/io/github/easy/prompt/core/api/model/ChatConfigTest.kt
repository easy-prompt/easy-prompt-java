package io.github.easy.prompt.core.api.model

import io.github.easy.prompt.core.api.model.template.ChatConfig
import io.github.easy.prompt.core.api.model.template.PromptParams
import org.junit.jupiter.api.Test

class ChatConfigTest {

    @Test
    fun fillSystemPrompts() {

        val chatConfig = ChatConfig(systemMessages = listOf("start \${message} end "))

        val fillSystemPrompts = chatConfig.fillSystemPrompts(PromptParams().addParams("message", "hello"))

        assert(fillSystemPrompts.size == 1)

    }

}