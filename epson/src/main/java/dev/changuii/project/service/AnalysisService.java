package dev.changuii.project.service;

import dev.changuii.project.entity.AnalysisDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AnalysisService {

        public AnalysisDTO analysisImage(Long imageId) throws IOException;
}
