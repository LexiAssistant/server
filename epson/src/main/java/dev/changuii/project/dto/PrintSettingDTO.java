package dev.changuii.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PrintSettingDTO {
    private String job_name;
    private String print_mode;
    private Map<String, Object> print_setting;

}
