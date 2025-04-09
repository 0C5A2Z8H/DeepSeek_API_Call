package com.prac.deepseek.Controller;

import com.prac.deepseek.pojo.CarCommandRequest;
import com.prac.deepseek.Service.DeepSeekService;
import com.prac.deepseek.pojo.ChatRequest;
import com.prac.deepseek.pojo.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.prac.deepseek.pojo.ChatRequest;

import java.util.Collections;
import java.util.Map;

@RestController
public class ChatController {

    @Autowired
    private DeepSeekService deepSeekService;

    @GetMapping("/chat")
    public ResponseEntity<String> chat() {
        String response = deepSeekService.getCompletion();
        return ResponseEntity.ok(response);
    }

    // 基础对话接口
    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        try {
            String response = deepSeekService.getChatResponse(userMessage);
            return ResponseEntity.ok(Collections.singletonMap("response", response));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // 带参数的进阶接口
    @PostMapping("/chat/advanced")
    public ResponseEntity<ChatResponse> advancedChat(@RequestBody ChatRequest request) {
        return ResponseEntity.ok(deepSeekService.handleAdvancedRequest(request));
    }

    @PostMapping("/car/command")
    public ResponseEntity<String> handleCarCommand(@RequestBody CarCommandRequest request) {
        try {
            // 调用 DeepSeekService 中的方法处理自然语言命令
            String commandJson = deepSeekService.generateCarCommandJson(request);
            return ResponseEntity.ok(commandJson);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("处理车辆命令失败: " + e.getMessage());
        }
    }

}