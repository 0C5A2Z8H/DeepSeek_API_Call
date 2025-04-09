package com.prac.deepseek.pojo;

import lombok.Data;

import java.util.List;

// ChatResponse.java
@Data
public class ChatResponse {
    private List<Choice> choices;

    // Getters and Setters
}