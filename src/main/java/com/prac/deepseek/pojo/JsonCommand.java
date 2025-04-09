package com.prac.deepseek.pojo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JsonCommand {
    private Integer carId;
    private String commandType;
    private Integer commandValue;
    private LocalDateTime updatedAt;

}
