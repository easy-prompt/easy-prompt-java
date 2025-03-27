package io.github.easy.prompt.core.api.model

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
data class ChatConfig(
    val temperature: Double = 0.0,
    val topP: Double? = 0.0,
    val maxTokens: Int = 0,
    val systemMessages: List<String> = emptyList(),
    val prompts: List<PromptStep> = emptyList()
)

/**
 * 提示步骤类
 */
data class PromptStep(
    val model: String = "",
    val images: List<String?> = emptyList(), // 使用可空类型，以处理空值
    val prompt: String = ""
)