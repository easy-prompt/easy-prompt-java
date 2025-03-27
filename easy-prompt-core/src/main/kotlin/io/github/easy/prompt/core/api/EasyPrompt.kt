package io.github.easy.prompt.core.api

import io.github.easy.prompt.core.api.model.HistoryChats
import io.github.easy.prompt.core.api.model.PromptParams
import io.github.easy.prompt.core.infrastructure.tools.yamlparse.YamlPromptParser

class EasyPrompt(
    private val yamlPromptParser: YamlPromptParser = YamlPromptParser()
) {

    fun invokePrompt(
        yamlTemplatePath: String, modelName: String,
        promptParams: PromptParams, historyChats: HistoryChats
    ) {

        // parsed YAML file, and acquire the prompt template
        val promptTemplate = yamlPromptParser.loadFromClasspath(yamlTemplatePath)

        // fill in the prompt template with the prompt parameters

        // invoke the LLM model to generate the answer

    }

}