package io.github.easy.prompt.core.infrastructure.utils.propread

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

/**
 * 从文件系统读取属性文件
 */
class FileSystemPropertiesReader(private val path: String) : AbstractPropertiesReader() {
    override fun getInputStream(): InputStream {
        return try {
            FileInputStream(path)
        } catch (e: Exception) {
            throw FileNotFoundException("在文件系统中找不到文件: $path")
        }
    }
}
