package io.github.easy.prompt.core.api.model

data class PromptParam(
    /**
     * prompt使用的key
     */
    private val key: String? = null,

    /**
     * prompt中需要使用的值
     */
    private val promptContent: String? = null
)

class PromptParams(
    private val promptParams: MutableList<PromptParam> = mutableListOf()
) {

    /**
     * 添加参数
     *
     * @param key           键
     * @param promptContent 提示词内容
     */
    fun addParams(key: String, promptContent: String): PromptParams {

        promptParams.add(PromptParam(key, promptContent))

        return this

    }

}
