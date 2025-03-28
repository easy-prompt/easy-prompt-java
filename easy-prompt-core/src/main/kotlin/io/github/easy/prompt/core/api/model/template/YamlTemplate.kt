package io.github.easy.prompt.core.api.model.template

/**
 * prompt template class
 *
 * 对应YAML文件的顶级结构
 * 使用Kotlin风格的属性名（遵循驼峰命名法）
 */
class PromptTemplate(
    val description: String = "",
    val chatConfig: ChatConfig = ChatConfig()
)

/**
 * 聊天配置类
 */
class ChatConfig(
    val model: String = "",
    val provider : String = "",
    val temperature: Double = 0.7,
    val topP: Double? = 0.0,
    val maxTokens: Int? = 0,
    val systemMessages: List<String> = emptyList(),
    val prompts: List<PromptStep> = emptyList()
) {
    /**
     * fill prompt template with params
     */
    fun fillSystemPrompts(params: PromptParams): List<String> {

        return systemMessages
            .map { systemMessage ->

                var fullPrompt = systemMessage;

                params.promptParams.forEach {

                    fullPrompt = fullPrompt.replace("\${${it.key}}", it.promptContent)

                }

                fullPrompt
            }
            .toList()


    }
}

/**
 * 提示步骤类
 */
class PromptStep(
    val model: String? = null,
    val provider : String? = null,
    val prompt: String? = null,
    val tools: List<String>? = emptyList(),
    val images: List<String>? = emptyList()
) {

    /**
     * fill prompt template with params
     */
    fun fillPrompts(params: PromptParams): String {

        var fullPrompt = prompt;

        params.promptParams.forEach {

            fullPrompt = fullPrompt?.replace("\${${it.key}}", it.promptContent)

        }

        return fullPrompt ?: ""

    }

}
