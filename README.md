# Easy Prompt

Easy Prompt is a Kotlin/Java framework that simplifies Large Language Model (LLM) prompt invocation. It provides a structured way to define, manage, and execute prompt templates, supports multiple LLM providers, and offers flexible configuration options.

## Features

- **YAML Template Definition**: Define structured prompt templates using YAML files
- **Multiple LLM Provider Support**: Built-in support for OpenAI-compatible interfaces, OpenRouter, and other LLM service providers
- **Parameterized Prompts**: Support for variables in prompts, enabling dynamic content replacement
- **Conversation History Management**: Automatic management of conversation history, supporting multi-turn dialogues
- **Streaming Responses**: Support for streaming response processing, receiving LLM-generated content in real-time
- **Flexible Configuration**: Support for configuring different models and parameters for different prompt steps

## Quick Start

### Add Dependency

Maven:
```xml
<dependency>
    <groupId>io.github.easy.prompt</groupId>
    <artifactId>easy-prompt-core</artifactId>
    <version>{latest-version}</version>
</dependency>
```

Gradle:
```kotlin
implementation("io.github.easy.prompt:easy-prompt-core:{latest-version}")
```

### Create YAML Prompt Template

Create a YAML file in the `src/main/resources/prompts` directory, for example `translate_prompt.yaml`:

```yaml
description: Prompt for translating economics and social science articles.
chatConfig:
  provider: openrouter
  model: qwen/qwen-2.5-72b-instruct
  temperature: 0.7
  maxTokens: 2000
  systemMessages:
    - |
      You are a professional translator fluent in Simplified Chinese who has worked on Chinese translations for The New York Times and The Economist. You have a deep understanding of translating news and current affairs articles. I would like you to help me translate the following English news paragraphs into Chinese, with a style similar to the Chinese editions of the aforementioned magazines.
      
      Translation rules:
      - This is an economic and political analysis article, please ensure the translation is accurate.
      - Accurately convey the news facts and background when translating.
      - Retain specific English terms or names with spaces before and after, for example: "中 UN 文".
  prompts:
    - # step1
      prompt: |
        Original content to translate:
        
        """
        ${content}
        """
    - # step2
      prompt: |
        Keep the translation result from the last step.
```

### Initialize and Call LLM

```kotlin
// Load properties from configuration file
val prop = PropertiesUtils.fromClasspath("app.local.properties")

// Create EasyPrompt instance and configure LLM providers
val easyPrompt = EasyPrompt(
    llmProviders = LLMProviders(
        llmClients = mutableListOf(
            OpenrouterLLMClient(
                apiUrl = prop.getProperty("openrouter.api.url", ""),
                apiKey = prop.getProperty("openrouter.api.key", ""),
            )
        )
    )
)

// Call the prompt template and pass parameters
val historyChats = easyPrompt.invokePrompt(
    "prompts/translate_prompt.yaml",
    PromptParams().addParams(
        "content",
        "My only vehicle I have right now is a 2021 RAM 2500 diesel. I would gladly trade it in for a Cybertruck just for the turning radius alone."
    ),
    HistoryChats()
)

// Get the last response
val lastResponse = historyChats.leastChatCompletion()
println(lastResponse?.answer)
```

## Core Concepts

### EasyPrompt

`EasyPrompt` is the main entry point of the framework, responsible for loading YAML templates and calling LLM services.

```kotlin
class EasyPrompt(
    private val llmProviders: LLMProviders = LLMProviders(),
    private val yamlPromptTemplateParser: YamlPromptTemplateParser = YamlPromptTemplateParser()
)
```

Main methods:
- `invokePrompt(yamlTemplatePath, promptParams, historyChats)`: Call the specified prompt template, pass parameters and history, and return updated history

### Prompt Template (PromptTemplate)

Prompt templates define the structure for interacting with LLMs, including system messages, prompt steps, and configuration parameters.

```kotlin
class PromptTemplate(
    val description: String = "",
    val chatConfig: ChatConfig = ChatConfig()
)
```

### Chat Configuration (ChatConfig)

Chat configuration defines the parameters for LLM calls, such as model, provider, temperature, etc.

```kotlin
class ChatConfig(
    val model: String = "",
    val provider: String = "",
    val temperature: Double = 0.7,
    val topP: Double? = 0.0,
    val maxTokens: Int? = 0,
    val systemMessages: List<String> = emptyList(),
    val prompts: List<PromptStep> = emptyList()
)
```

Main methods:
- `fillSystemPrompts(params)`: Fill variables in system messages with provided parameters

### Prompt Step (PromptStep)

Prompt steps define the content and configuration for a single prompt.

```kotlin
class PromptStep(
    val model: String? = null,
    val provider: String? = null,
    val prompt: String? = null,
    val images: List<String>? = emptyList()
)
```

Main methods:
- `fillPrompts(params)`: Fill variables in prompts with provided parameters

### Prompt Parameters (PromptParams)

Prompt parameters are used to replace variables in prompt templates.

```kotlin
class PromptParams(
    val promptParams: MutableList<PromptParam> = mutableListOf()
) {
    fun addParams(key: String, promptContent: String): PromptParams
}
```

### Chat History (HistoryChats)

Chat history records the interaction history with the LLM.

```kotlin
class HistoryChats(
    val historyChats: MutableList<ChatCompletion> = mutableListOf()
) {
    fun appendChat(chatModel: String, prompt: String, answer: String?, reasoning: String?, images: List<String> = emptyList())
    fun leastChatCompletion(): ChatCompletion?
}
```

## Supported LLM Providers

Easy Prompt supports multiple LLM providers through the `ILLMClient` interface:

- **OpenAI-compatible Interface** (`OpenAICompatibleLLMClient`): Supports all services compatible with the OpenAI API
- **OpenRouter** (`OpenrouterLLMClient`): Supports access to multiple models through OpenRouter
- More providers are in development...

## Custom LLM Client

You can create custom LLM clients by implementing the `ILLMClient` interface:

```kotlin
interface ILLMClient {
    fun invoke(
        systemPrompts: List<String> = emptyList(), 
        fullPrompt: String,
        historyChats: HistoryChats = HistoryChats(),
        promptInvokeParam: PromptInvokeParam,
        streamingHandler: StreamingHandler = EmptyStreamingHandler()
    ): ChatCompletion

    fun providerName(): String
}
```

Then add your custom client to `LLMProviders`:

```kotlin
val llmProviders = LLMProviders(
    llmClients = mutableListOf(
        YourCustomLLMClient(),
        OpenrouterLLMClient(apiUrl, apiKey)
    )
)
```

## Advanced Usage

### Streaming Response Processing

```kotlin
val streamingHandler = object : StreamingHandler {
    override fun onNext(data: Any) {
        // Process each part of the streaming response
        print(data)
    }

    override fun onComplete(data: Any) {
        // Handle completion event
        println("\nResponse completed")
    }
}

// Pass streamingHandler when invoking
val chatCompletion = llmClient.invoke(
    systemPrompts = listOf("You are a helpful assistant"),
    fullPrompt = "Tell me a joke",
    promptInvokeParam = promptInvokeParam,
    streamingHandler = streamingHandler
)
```

### Multi-step Prompts

YAML templates support defining multiple prompt steps, each with different models and providers:

```yaml
chatConfig:
  provider: openai
  model: gpt-4
  prompts:
    - # First step uses default configuration
      prompt: "Generate a story idea"
    - # Second step uses a different model
      model: gpt-3.5-turbo
      prompt: "Expand on the previous idea"
```

### Using History for Multi-turn Conversations

```kotlin
// First round of conversation
var historyChats = easyPrompt.invokePrompt(
    yamlTemplatePath = "prompts/chat_prompt.yaml",
    promptParams = PromptParams().addParams("query", "Hello, please introduce yourself"),
    historyChats = HistoryChats()
)

// Second round of conversation, using the history from the previous round
historyChats = easyPrompt.invokePrompt(
    yamlTemplatePath = "prompts/chat_prompt.yaml",
    promptParams = PromptParams().addParams("query", "I'd like to know more about your capabilities"),
    historyChats = historyChats  // Pass the history from the previous round
)
```

## Configuration Files

Easy Prompt supports configuration through property files. You can create configuration files in the `src/main/resources` directory, such as `app.properties` or `app.local.properties` (for local development environments):

```properties
# OpenRouter configuration
openrouter.api.url=https://openrouter.ai/api/v1
openrouter.api.key=your-api-key-here

# OpenAI-compatible interface configuration
openai.api.url=https://api.openai.com/v1
openai.api.key=your-api-key-here
```

Then use `PropertiesUtils` to load the configuration:

```kotlin
// Load configuration file from classpath
val prop = PropertiesUtils.fromClasspath("app.local.properties")

// Initialize LLM client with configuration
val llmProviders = LLMProviders(
    llmClients = mutableListOf(
        OpenrouterLLMClient(
            apiUrl = prop.getProperty("openrouter.api.url", ""),
            apiKey = prop.getProperty("openrouter.api.key", "")
        )
    )
)
```

## Contributing

Contributions are welcome, including code contributions, issue reports, or improvement suggestions. Please follow the project's contribution guidelines.

## License

This project is licensed under the [Apache License 2.0](LICENSE).

```
Copyright [yyyy] [name of copyright owner]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
