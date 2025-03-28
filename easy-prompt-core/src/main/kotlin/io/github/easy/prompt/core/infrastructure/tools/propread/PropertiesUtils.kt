package io.github.easy.prompt.core.infrastructure.tools.propread

/**
 * Properties工具类，提供方便的静态方法读取属性
 */
object PropertiesUtils {

    /**
     * 从classpath读取属性文件
     * @param path classpath中的路径 (如 "application.properties" 或 "/config/app.properties")
     */
    fun fromClasspath(path: String): PropertiesReader = ClasspathPropertiesReader(path)

    /**
     * 从文件系统读取属性文件
     * @param path 文件系统中的路径 (如 "/etc/app/config.properties")
     */
    fun fromFileSystem(path: String): PropertiesReader = FileSystemPropertiesReader(path)

}