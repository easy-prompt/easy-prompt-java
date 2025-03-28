package io.github.easy.prompt.core.infrastructure.tools.propread

import java.io.FileNotFoundException
import java.io.InputStream

/**
 * 从Classpath读取属性文件
 */
class ClasspathPropertiesReader(private val path: String) : AbstractPropertiesReader() {

    override fun getInputStream(): InputStream {
        // 尝试两种方式读取classpath资源
        // 1. 使用ClassLoader
        val classLoader = Thread.currentThread().contextClassLoader ?: ClasspathPropertiesReader::class.java.classLoader
        val normalizedPath = if (path.startsWith("/")) path.substring(1) else path

        return classLoader.getResourceAsStream(normalizedPath)
        // 2. 如果第一种方式失败，尝试使用Class.getResourceAsStream
            ?: ClasspathPropertiesReader::class.java.getResourceAsStream(
                if (path.startsWith("/")) path else "/$path"
            )
            ?: throw FileNotFoundException("在classpath中找不到资源文件: $path")
    }

}
