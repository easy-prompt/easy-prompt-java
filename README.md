# Easy Prompt

Easy Prompt是一个简化大语言模型(LLM)提示词调用的Kotlin/Java框架。它提供了一种结构化的方式来定义、管理和执行提示词模板，支持多种LLM提供商，并具有灵活的配置选项。

## 特性

- **YAML模板定义**：使用YAML文件定义结构化的提示词模板
- **多LLM提供商支持**：内置支持OpenAI兼容接口、OpenRouter等多种LLM服务提供商
- **参数化提示词**：支持在提示词中使用变量，实现动态内容替换
- **会话历史管理**：自动管理对话历史，支持多轮对话
- **流式响应**：支持流式响应处理，实时获取LLM生成内容
- **灵活配置**：支持为不同提示步骤配置不同的模型和参数

## 快速开始

### 添加依赖

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

### 创建YAML提示词模板

在`src/main/resources/prompts`目录下创建YAML文件，例如`translate_prompt.yaml`:

```yaml
description: 经济社科类文章翻译用的Prompt。
chatConfig:
  provider: openrouter
  model: qwen/qwen-2.5-72b-instruct
  temperature: 0.7
  maxTokens: 2000
  systemMessages:
    - |
      你是一位精通简体中文的专业翻译，曾参与《纽约时报》和《经济学人》中文版的翻译工作，因此对于新闻和时事文章的翻译有深入的理解。我希望你能帮我将以下英文新闻段落翻译成中文，风格与上述杂志的中文版相似。
      
      翻译规则:
      - 这是一篇经济、时政分析的文章，请保证翻译的内容准确无误。
      - 翻译时要准确传达新闻事实和背景。
      - 保留特定的英文术语或名字，并在其前后加上空格，例如："中 UN 文"。
  prompts:
    - # step1
      prompt: |
        需要翻译的原文内容:
        
        """
        ${content}
        """
    - # step2
      prompt: |
        保留最后一步的翻译结果。
```

### 初始化并调用LLM

```kotlin
// 从配置文件加载属性
val prop = PropertiesUtils.fromClasspath("app.local.properties")

// 创建EasyPrompt实例并配置LLM提供商
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

// 调用提示词模板并传递参数
val historyChats = easyPrompt.invokePrompt(
    "prompts/translate_prompt.yaml",
    PromptParams().addParams(
        "content",
        "My only vehicle I have right now is a 2021 RAM 2500 diesel. I would gladly trade it in for a Cybertruck just for the turning radius alone."
    ),
    HistoryChats()
)

// 获取最后一次回复
val lastResponse = historyChats.leastChatCompletion()
println(lastResponse?.answer)
```

## 核心概念

### EasyPrompt

`EasyPrompt`是框架的主要入口点，负责加载YAML模板并调用LLM服务。

```kotlin
class EasyPrompt(
    private val llmProviders: LLMProviders = LLMProviders(),
    private val yamlPromptTemplateParser: YamlPromptTemplateParser = YamlPromptTemplateParser()
)
```

主要方法：
- `invokePrompt(yamlTemplatePath, promptParams, historyChats)`: 调用指定的提示词模板，传入参数和历史记录，返回更新后的历史记录

### 提示词模板 (PromptTemplate)

提示词模板定义了与LLM交互的结构，包括系统消息、提示步骤和配置参数。

```kotlin
class PromptTemplate(
    val description: String = "",
    val chatConfig: ChatConfig = ChatConfig()
)
```

### 聊天配置 (ChatConfig)

聊天配置定义了LLM调用的参数，如模型、提供商、温度等。

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

主要方法：
- `fillSystemPrompts(params)`: 使用提供的参数填充系统消息中的变量

### 提示步骤 (PromptStep)

提示步骤定义了单个提示的内容和配置。

```kotlin
class PromptStep(
    val model: String? = null,
    val provider: String? = null,
    val prompt: String? = null,
    val images: List<String>? = emptyList()
)
```

主要方法：
- `fillPrompts(params)`: 使用提供的参数填充提示词中的变量

### 提示参数 (PromptParams)

提示参数用于在提示词模板中替换变量。

```kotlin
class PromptParams(
    val promptParams: MutableList<PromptParam> = mutableListOf()
) {
    fun addParams(key: String, promptContent: String): PromptParams
}
```

### 聊天历史 (HistoryChats)

聊天历史记录了与LLM的交互历史。

```kotlin
class HistoryChats(
    val historyChats: MutableList<ChatCompletion> = mutableListOf()
) {
    fun appendChat(chatModel: String, prompt: String, answer: String?, reasoning: String?, images: List<String> = emptyList())
    fun leastChatCompletion(): ChatCompletion?
}
```

## 支持的LLM提供商

Easy Prompt通过`ILLMClient`接口支持多种LLM提供商：

- **OpenAI兼容接口** (`OpenAICompatibleLLMClient`): 支持所有兼容OpenAI API的服务
- **OpenRouter** (`OpenrouterLLMClient`): 支持通过OpenRouter访问多种模型
- 更多提供商正在开发中...

## 自定义LLM客户端

您可以通过实现`ILLMClient`接口来创建自定义的LLM客户端：

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

然后将您的自定义客户端添加到`LLMProviders`中：

```kotlin
val llmProviders = LLMProviders(
    llmClients = mutableListOf(
        YourCustomLLMClient(),
        OpenrouterLLMClient(apiUrl, apiKey)
    )
)
```

## 高级用法

### 流式响应处理

```kotlin
val streamingHandler = object : StreamingHandler {
    override fun onNext(data: Any) {
        // 处理流式响应的每个部分
        print(data)
    }

    override fun onComplete(data: Any) {
        // 处理完成事件
        println("\nResponse completed")
    }
}

// 在调用时传入streamingHandler
val chatCompletion = llmClient.invoke(
    systemPrompts = listOf("You are a helpful assistant"),
    fullPrompt = "Tell me a joke",
    promptInvokeParam = promptInvokeParam,
    streamingHandler = streamingHandler
)
```

### 多步骤提示词

YAML模板支持定义多个提示步骤，每个步骤可以使用不同的模型和提供商：

```yaml
chatConfig:
  provider: openai
  model: gpt-4
  prompts:
    - # 第一步使用默认配置
      prompt: "Generate a story idea"
    - # 第二步使用不同的模型
      model: gpt-3.5-turbo
      prompt: "Expand on the previous idea"
```

### 使用历史记录进行多轮对话

```kotlin
// 第一轮对话
var historyChats = easyPrompt.invokePrompt(
    yamlTemplatePath = "prompts/chat_prompt.yaml",
    promptParams = PromptParams().addParams("query", "你好，请介绍一下自己"),
    historyChats = HistoryChats()
)

// 第二轮对话，使用上一轮的历史记录
historyChats = easyPrompt.invokePrompt(
    yamlTemplatePath = "prompts/chat_prompt.yaml",
    promptParams = PromptParams().addParams("query", "我想了解更多关于你的能力"),
    historyChats = historyChats  // 传入上一轮的历史记录
)
```

## 配置文件

Easy Prompt支持通过属性文件进行配置。您可以在`src/main/resources`目录下创建配置文件，例如`app.properties`或`app.local.properties`（用于本地开发环境）：

```properties
# OpenRouter配置
openrouter.api.url=https://openrouter.ai/api/v1
openrouter.api.key=your-api-key-here

# OpenAI兼容接口配置
openai.api.url=https://api.openai.com/v1
openai.api.key=your-api-key-here
```

然后使用`PropertiesUtils`加载配置：

```kotlin
// 从类路径加载配置文件
val prop = PropertiesUtils.fromClasspath("app.local.properties")

// 使用配置初始化LLM客户端
val llmProviders = LLMProviders(
    llmClients = mutableListOf(
        OpenrouterLLMClient(
            apiUrl = prop.getProperty("openrouter.api.url", ""),
            apiKey = prop.getProperty("openrouter.api.key", "")
        )
    )
)
```

## 贡献

欢迎贡献代码、报告问题或提出改进建议。请遵循项目的贡献指南。

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。

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
```
