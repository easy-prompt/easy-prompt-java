package io.github.easy.prompt.core.infrastructure.tools.yamlparse

import io.github.easy.prompt.core.infrastructure.tools.yamlparse.YamlPromptTemplateParser
import org.junit.jupiter.api.Test

class YamlPromptTemplateParserTest {

    @Test
    fun loadFromFile() {

        val parser = YamlPromptTemplateParser()

        val result = parser.loadFromClasspath("prompts/translate_prompt.yaml")

        println(result)
    }
}