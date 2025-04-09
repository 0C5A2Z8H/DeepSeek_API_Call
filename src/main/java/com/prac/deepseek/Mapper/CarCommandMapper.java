package com.prac.deepseek.Mapper;

import com.prac.deepseek.pojo.JsonCommand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface CarCommandMapper {
    @Insert({
            "<script>",
            "INSERT INTO car_command_bit (car_id, command_type, command_value, updated_at)",
            "VALUES",
            "<foreach collection='commands' item='cmd' separator=','>",
            "(#{cmd.carId}, #{cmd.commandType}, #{cmd.commandValue}, #{cmd.updatedAt})",
            "</foreach>",
            "</script>"
    })
    int insertCarCommands(List<JsonCommand> commands);

}
