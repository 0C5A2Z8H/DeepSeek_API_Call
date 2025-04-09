# DeepSeek_API_Call
可以应用于SpringBoot后端的DeepSeek api调用项目，同时集成了自然语言转换成json的高级功能。
## 简介

本项目是一个基于 Java Spring Boot 的应用程序，旨在调用 Deepseek API 进行交互。

## 主要功能

*   调用 Deepseek API 获取信息或执行任务 (具体功能取决于 `DeepseekService` 的实现)。
*   解析来自 API 或其他来源的 JSON 数据 (例如车辆指令 `JsonCommand`)。
*   使用 MyBatis 将处理后的数据批量插入到 MySQL 数据库的 `car_command_bit` 表中。

## 技术栈

*   **语言:** Java 17
*   **框架:** Spring Boot 3.2.4
*   **构建工具:** Maven
*   **数据持久化:** MyBatis, MySQL Connector/J
*   **Web/API:** Spring Web
*   **JSON 处理:** Jackson Databind
*   **开发辅助:** Lombok

## 环境准备

1.  **JDK:** 确保已安装 Java 17 或更高版本，并配置好 `JAVA_HOME` 环境变量。
2.  **Maven:** 确保已安装 Maven，并配置好环境变量。
3.  **MySQL 数据库:**
    *   需要一个正在运行的 MySQL 数据库实例。

## 配置

主要的应用程序配置位于 `src/main/resources/application.yml` 文件中。你需要根据你的环境修改以下配置：

*   `deepseek.api.key`: 你的 Deepseek API 密钥。
*   `deepseek.api.url`: Deepseek API 的端点 URL 
*   `spring.datasource.url`: 你的 MySQL 数据库连接 URL 
*   `spring.datasource.username`: 数据库用户名。
*   `spring.datasource.password`: 数据库密码。

## 如何运行

1.  **清理并构建项目:**
    ```bash
    mvn clean install
    ```
2.  **运行应用程序:**
    ```bash
    mvn spring-boot:run
    ```
    应用程序将在默认端口 (通常是 8080) 启动。
