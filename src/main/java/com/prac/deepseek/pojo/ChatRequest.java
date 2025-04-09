package com.prac.deepseek.pojo;

import lombok.Data;

import java.util.List;


// ChatRequest.java
@Data
public class ChatRequest {
    private String model;
    private List<Message> messages;
    private Double temperature;  // 新增参数（0-2）
    private Integer max_tokens;  // 最大返回token数
    private Boolean stream;
}

