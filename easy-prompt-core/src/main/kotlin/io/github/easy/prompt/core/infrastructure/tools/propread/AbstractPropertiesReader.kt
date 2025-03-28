package io.github.easy.prompt.core.infrastructure.tools.propread

import java.io.InputStream
import java.lang.RuntimeException
import java.util.*

/**
 * 从不同源读取properties的抽象基类
 */
abstract class AbstractPropertiesReader : PropertiesReader {
    private var propertiesCache: Properties? = null

    override fun read(): Properties {

        // 简单的缓存机制，避免重复读取
        if (propertiesCache != null) {
            return propertiesCache!!
        }

        val properties = Properties()

        try {
            getInputStream().use { inputStream ->
                properties.load(inputStream)
            }

            propertiesCache = properties

        } catch (e: Exception) {
            throw RuntimeException("读取属性文件失败", e)
        }

        return propertiesCache!!

    }

    override fun getProperty(key: String): String? {
        return read().getProperty(key)
    }

    override fun getProperty(key: String, defaultValue: String): String {
        return read().getProperty(key, defaultValue)
    }

    /**
     * 获取输入流，由子类实现
     */
    protected abstract fun getInputStream(): InputStream
}
