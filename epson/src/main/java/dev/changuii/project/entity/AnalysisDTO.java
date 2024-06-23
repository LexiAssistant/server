package dev.changuii.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor @NoArgsConstructor
public class AnalysisDTO {

    private List<String> keywords;
    private List<String> topKewords;
    private List<String> summary;

}
