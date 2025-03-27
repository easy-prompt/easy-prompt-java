package io.github.easy.prompt.core.yamlparse

import io.github.easy.prompt.core.infrastructure.tools.yamlparse.YamlPromptParser
import org.junit.jupiter.api.Test

class YamlPromptParserTest {

    @Test
    fun loadFromFile() {

        val parser = YamlPromptParser()

        val result = parser.loadFromClasspath("prompts/translate_prompt.yaml")

        println(result)
    }
}