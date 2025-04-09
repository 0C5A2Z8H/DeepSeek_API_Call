package com.prac.deepseek.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prac.deepseek.pojo.CarCommandRequest;
import com.prac.deepseek.pojo.ChatRequest;
import com.prac.deepseek.pojo.ChatResponse;
import com.prac.deepseek.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DeepSeekService {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    public String getCompletion() {
        // 构造请求体
        ChatRequest request = new ChatRequest();
        request.setModel("deepseek-reasoner");
        request.setMessages(Collections.singletonList(
                new Message("user", "你是帮我解决问题的助手，以后要说汉语，每次对话开头需要叫我欧尼酱。")
        ));

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 发送请求
        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);
        try {
            ChatResponse response = restTemplate.postForObject(apiUrl, entity, ChatResponse.class);
            return Optional.ofNullable(response)
                    .map(ChatResponse::getChoices)
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0).getMessage().getContent())
                    .orElse("No response received");
        } catch (RestClientException e) {
            return "API调用失败: " + e.getMessage();
        }
    }

    // 维护对话历史（简单内存存储）
    private final List<Message> conversationHistory = new ArrayList<>();

    public String getChatResponse(String userMessage) {
        // 构建对话历史
        conversationHistory.add(new Message("user", userMessage));

        // 构造请求
        ChatRequest request = new ChatRequest();
        request.setModel("deepseek-chat");
        request.setMessages(conversationHistory);
        request.setTemperature(0.7);  // 新增参数

        // 发送请求
        ChatResponse response = sendRequest(request);

        // 保存AI响应到历史
        String aiResponse = response.getChoices().get(0).getMessage().getContent();
        conversationHistory.add(new Message("assistant", aiResponse));

        return aiResponse;
    }

    private ChatResponse sendRequest(ChatRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);

        // 添加超时和重试配置
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10))
                .build();

        return restTemplate.postForObject(apiUrl, entity, ChatResponse.class);
    }

    // 新增方法：处理带完整参数的请求
    public ChatResponse handleAdvancedRequest(ChatRequest request) {
        // 参数校验
        if (request.getModel() == null) {
            request.setModel("deepseek-chat"); // 设置默认模型
        }

        // 发送请求（复用已有方法）
        ChatResponse response = sendRequest(request);

        // 如果需要保存到历史记录
        if (request.getMessages() != null && !request.getMessages().isEmpty()) {
            conversationHistory.addAll(request.getMessages());
        }

        return response;
    }

    /**
     * 调用 deepseek 接口，将自然语言命令转换为所需的 JSON 格式，
     * 以 carId、commandType、commandValue、updatedAt 等字段描述命令。
     */
    public String generateCarCommandJson(CarCommandRequest request) {
        String systemPrompt =
                "你是一个 JSON 命令转换器，请将用户的自然语言命令转换为标准的 JSON 数组。" +
                        "要求：" +
                        "1. 直接输出 JSON 数组，不要包含任何 markdown 格式或其他说明文字；" +
                        "2. 每个命令对象必须包含: carId(number), commandType(VarChar), commandValue(number), updatedAt(datetime)；" +
                        "3. 对接命令(dock)和解体命令(breakUp)需要输出两条互相对应的指令。比如说carId=1的组内commandValue为5，carId=5的组内commandValue为1"+
                        "4. updatedAt的返回值直接使用Body中的updatedAt取值"+
                        "5. 命令共有三种：dock(对接)、navigate(巡航)、breakUp(解体)。commandType必须为这三个英文之一。从naturalCarCommand提取最适合的英文" +
                        "6. 当命令类型为navigate时，commandValue为null" ;
        String userPrompt = String.format("%s，更新时间为：%s",
        request.getNaturalCarCommand(),
        request.getUpdatedAt());

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", systemPrompt));
        messages.add(new Message("user", userPrompt));


        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("deepseek-reasoner");
        chatRequest.setMessages(messages);
        chatRequest.setTemperature(0.1);


        ChatResponse response = sendRequest(chatRequest);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            throw new RuntimeException("无法生成车辆命令");
        }

        String aiResponse = response.getChoices().get(0).getMessage().getContent()
                .replaceAll("```json", "") // 移除可能的 markdown 标记
                .replaceAll("```", "")
                .trim();

        try {
            // 验证并格式化 JSON
            Object jsonObject = objectMapper.readValue(aiResponse, Object.class);
            return objectMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            throw new RuntimeException("生成的命令格式不正确: " + e.getMessage());
        }
    }

}