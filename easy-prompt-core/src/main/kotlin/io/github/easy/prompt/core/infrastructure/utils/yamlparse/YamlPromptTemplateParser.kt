package io.github.easy.prompt.core.infrastructure.utils.yamlparse

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.easy.prompt.core.api.model.template.PromptTemplate
import java.io.File
import java.io.InputStream

/**
 * YAML配置文件解析工具类
 * 使用Jackson库解析自定义格式的YAML配置文件，支持驼峰命名法
 */
class YamlPromptTemplateParser {

    // 创建并配置YAML ObjectMapper实例，使用驼峰命名策略
    private val yamlObjectMapper = ObjectMapper(YAMLFactory()).apply {
        registerModule(KotlinModule.Builder().build())

        // 配置属性命名策略为驼峰命名法
        propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE

        // 配置反序列化特性
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    /**
     * 从文件路径加载YAML配置
     * @param filePath YAML文件路径
     * @return 解析后的PromptConfig对象
     * @throws IllegalArgumentException 当文件不存在或解析失败时
     */
    fun loadFromFile(filePath: String): PromptTemplate {
        val file = File(filePath)
        if (!file.exists()) {
            throw IllegalArgumentException("Config file not found: $filePath")
        }

        return try {
            yamlObjectMapper.readValue(file)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse YAML configuration from file: ${e.message}", e)
        }
    }

    /**
     * 从输入流加载YAML配置
     * @param inputStream 包含YAML内容的输入流
     * @return 解析后的PromptConfig对象
     */
    fun loadFromStream(inputStream: InputStream): PromptTemplate {
        return try {
            yamlObjectMapper.readValue(inputStream)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse YAML configuration from stream: ${e.message}", e)
        }
    }

    /**
     * 从字符串加载YAML配置
     * @param yamlContent 包含YAML内容的字符串
     * @return 解析后的PromptConfig对象
     */
    fun loadFromString(yamlContent: String): PromptTemplate {
        return try {
            yamlObjectMapper.readValue(yamlContent)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse YAML configuration from string: ${e.message}", e)
        }
    }

    /**
     * 从类路径加载YAML配置
     * @param resourcePath 资源路径，例如 "config/prompt.yaml"
     * @return 解析后的PromptConfig对象
     * @throws IllegalArgumentException 当资源不存在或解析失败时
     */
    fun loadFromClasspath(resourcePath: String): PromptTemplate {
        // 使用当前类的类加载器
        val classLoader = this.javaClass.classLoader
        return loadFromClasspathWithLoader(resourcePath, classLoader)
    }

    /**
     * 从类路径加载YAML配置，使用指定的上下文类的类加载器
     * @param resourcePath 资源路径，例如 "config/prompt.yaml"
     * @param contextClass 用于获取类加载器的类
     * @return 解析后的PromptConfig对象
     */
    fun loadFromClasspath(resourcePath: String, contextClass: Class<*>): PromptTemplate {
        val classLoader = contextClass.classLoader
        return loadFromClasspathWithLoader(resourcePath, classLoader)
    }

    /**
     * 从类路径加载YAML配置，使用指定的类加载器
     * 这是一个私有辅助方法，被其他loadFromClasspath方法使用
     */
    private fun loadFromClasspathWithLoader(resourcePath: String, classLoader: ClassLoader): PromptTemplate {
        // 尝试获取资源的输入流
        val resourceStream = classLoader.getResourceAsStream(resourcePath)
            ?: throw IllegalArgumentException("Resource not found in classpath: $resourcePath")

        // 使用输入流加载配置
        return try {
            resourceStream.use { stream ->
                loadFromStream(stream)
            }
        } catch (e: Exception) {
            throw IllegalArgumentException(
                "Failed to parse YAML configuration from classpath resource $resourcePath: ${e.message}",
                e
            )
        }
    }

    /**
     * 从类路径加载YAML配置的泛型扩展方法
     * @param resourcePath 资源路径，例如 "config/prompt.yaml"
     * @return 解析后的PromptConfig对象
     */
    inline fun <reified T> loadFromClasspathWithType(resourcePath: String): PromptTemplate {
        return loadFromClasspath(resourcePath, T::class.java)
    }

}