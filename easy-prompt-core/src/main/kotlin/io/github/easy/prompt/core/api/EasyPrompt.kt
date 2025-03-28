package io.github.easy.prompt.core.api

import io.github.easy.prompt.core.api.model.template.HistoryChats
import io.github.easy.prompt.core.api.model.invoke.PromptInvokeParam
import io.github.easy.prompt.core.api.model.template.PromptParams
import io.github.easy.prompt.core.infrastructure.provider.LLMProviders
import io.github.easy.prompt.core.infrastructure.tools.yamlparse.YamlPromptTemplateParser

class EasyPrompt(
    private val llmProviders: LLMProviders = LLMProviders(),
    private val yamlPromptTemplateParser: YamlPromptTemplateParser = YamlPromptTemplateParser()
) {

    /**
     * Invoke the prompt template with the given parameters and history chats.
     *
     * @param yamlTemplatePath The classpath to the YAML template file.
     */
    fun invokePrompt(
        yamlTemplatePath: String,
        promptParams: PromptParams = PromptParams(),
        historyChats: HistoryChats = HistoryChats()
    ): HistoryChats {

        // parsed YAML file, and acquire the prompt template
        val promptTemplate = yamlPromptTemplateParser.loadFromClasspath(yamlTemplatePath)

        // fill in the system prompts with the prompt parameters
        val fullSystemPrompts = promptTemplate.chatConfig.fillSystemPrompts(promptParams)

        // iterate through each prompt step in the prompt template
        promptTemplate.chatConfig.prompts.forEach {

            // fill in the prompt template with the prompt parameters
            val fullPrompt = it.fillPrompts(promptParams)

            val promptInvokeParam = PromptInvokeParam.from(promptTemplate.chatConfig, it)

            // invoke the LLM model to generate the answer
            val chatCompletion = llmProviders
                .acquireLLMClient(promptInvokeParam.provider)
                .invoke(fullSystemPrompts, fullPrompt, historyChats, promptInvokeParam)

            historyChats.historyChats.add(chatCompletion)

        }

        return historyChats

    }

}