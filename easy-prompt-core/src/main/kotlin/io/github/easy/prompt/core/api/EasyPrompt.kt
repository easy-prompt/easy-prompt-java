package io.github.easy.prompt.core.api

import io.github.easy.prompt.core.api.model.HistoryChats
import io.github.easy.prompt.core.api.model.PromptParams
import io.github.easy.prompt.core.infrastructure.provider.ILLMClient
import io.github.easy.prompt.core.infrastructure.tools.yamlparse.YamlPromptParser

class EasyPrompt(
    private val llmClient: ILLMClient,
    private val yamlPromptParser: YamlPromptParser = YamlPromptParser()
) {

    fun invokePrompt(
        yamlTemplatePath: String,
        promptParams: PromptParams = PromptParams(),
        historyChats: HistoryChats = HistoryChats()
    ): HistoryChats {

        // parsed YAML file, and acquire the prompt template
        val promptTemplate = yamlPromptParser.loadFromClasspath(yamlTemplatePath)

        // fill in the system prompts with the prompt parameters
        val fullSystemPrompts = promptTemplate.chatConfig.fillSystemPrompts(promptParams)

        // iterate through each prompt step in the prompt template
        promptTemplate.chatConfig.prompts.forEach {

            // fill in the prompt template with the prompt parameters
            val fullPrompt = it.fillPrompts(promptParams)

            // it.model ?: promptTemplate.chatConfig.model

            // invoke the LLM model to generate the answer
            val chatCompletion = llmClient.invoke(
                fullSystemPrompts, fullPrompt,
                historyChats
            )

            historyChats.historyChats.add(chatCompletion)

        }

        return historyChats

    }

}