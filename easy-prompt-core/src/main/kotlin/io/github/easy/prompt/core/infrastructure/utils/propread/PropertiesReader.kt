package io.github.easy.prompt.core.infrastructure.utils.propread

import java.util.*

/**
 * Properties读取接口，定义读取属性文件的基本行为
 */
interface PropertiesReader {

    /**
     * 读取属性
     * @return 属性集合
     */
    fun read(): Properties

    /**
     * 获取特定的属性值
     * @param key 属性键
     * @return 属性值，如果不存在则返回null
     */
    fun getProperty(key: String): String?

    /**
     * 获取特定的属性值，如果不存在则返回默认值
     * @param key 属性键
     * @param defaultValue 默认值
     * @return 属性值或默认值
     */
    fun getProperty(key: String, defaultValue: String): String
}
