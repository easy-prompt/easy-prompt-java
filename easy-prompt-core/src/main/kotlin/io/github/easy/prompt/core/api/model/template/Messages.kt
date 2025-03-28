package io.github.easy.prompt.core.api.model.template

/**
 * 代表LLM聊天完成后的回应
 */
data class ChatCompletion(
    val chatModel: String,
    val prompt: String,
    val answer: String? = "",
    val reasoning: String? = "",
    val images: List<String>? = emptyList()
)

/**
 * 代表聊天记录
 */
class HistoryChats(
    val historyChats: MutableList<ChatCompletion> = mutableListOf()
) {

    /**
     * 添加一条聊天记录
     * @param chatModel 模型名称
     * @param prompt 提示词
     * @param answer 回应
     * @param reasoning 理由
     * @param images 图像
     */
    fun appendChat(
        chatModel: String, prompt: String,
        answer: String? = null, reasoning: String? = null, images: List<String> = emptyList()
    ) {

        historyChats.add(
            ChatCompletion(chatModel, prompt, answer, reasoning, images)
        )

    }

    /**
     * 获取最后一条聊天记录
     */
    fun leastChatCompletion(): ChatCompletion? = historyChats.lastOrNull()

}