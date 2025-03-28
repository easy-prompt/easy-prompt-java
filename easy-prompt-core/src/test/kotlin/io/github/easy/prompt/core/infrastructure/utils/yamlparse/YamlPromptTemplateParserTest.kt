package io.github.easy.prompt.core.infrastructure.utils.yamlparse

import org.junit.jupiter.api.Test

class YamlPromptTemplateParserTest {

    @Test
    fun loadFromFile() {

        val parser = YamlPromptTemplateParser()

        val result = parser.loadFromClasspath("prompts/translate_prompt.yaml")

        println(result)
    }
}