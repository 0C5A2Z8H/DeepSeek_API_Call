package com.prac.deepseek.pojo;

import lombok.Data;

@Data
public class CarCommandRequest {
    private String naturalCarCommand;
    private String updatedAt;   // 根据需要，可能使用更合适的时间类型


    // 手动添加 getter 和 setter
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNaturalCarCommand() {
        return naturalCarCommand;
    }

    public void setNaturalCarCommand(String naturalCarCommand) {
        this.naturalCarCommand = naturalCarCommand;
    }

}
