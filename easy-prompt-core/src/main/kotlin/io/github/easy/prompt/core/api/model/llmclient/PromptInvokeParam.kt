package io.github.easy.prompt.core.api.model.llmclient

import io.github.easy.prompt.core.api.model.template.ChatConfig
import io.github.easy.prompt.core.api.model.template.PromptStep

data class PromptInvokeParam(
    val model: String = "",
    val provider: String = "",
    val temperature: Double = 0.7,
    val topP: Double? = null,
    val maxTokens: Int? = 1000
) {

    companion object {

        fun from(chatConfig: ChatConfig, promptStep: PromptStep): PromptInvokeParam {

            // if promptStep prop exist use promptStep prop,  else use chatConfig prop
            return PromptInvokeParam(
                model =  promptStep.model ?: chatConfig.model ,
                provider =  promptStep.provider ?: chatConfig.provider,
                temperature = chatConfig.temperature,
                topP = chatConfig.topP,
                maxTokens = chatConfig.maxTokens
            )

        }

    }

}