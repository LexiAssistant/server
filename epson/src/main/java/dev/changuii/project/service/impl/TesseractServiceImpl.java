package dev.changuii.project.service.impl;

import dev.changuii.project.service.TesseractService;
import jakarta.annotation.PostConstruct;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class TesseractServiceImpl implements TesseractService {

    @Value("${tesseract.jna-path}")
    private String jnaPath;

    @Value("${tesseract.language-path}")
    private String languagePath;

    @Value("${tesseract.file-path}")
    private String filePath;


    @PostConstruct
    public void init(){
        System.setProperty("jna.library.path", jnaPath);
    }

    @Override
    public String imageToString(MultipartFile mfile) throws IOException {
        File f = this.multipartFileToFile(mfile);

        Tesseract tesseract = getTesseract();

        System.out.println(f.getName() + " " + f.exists() + " " + f.canRead());
        System.out.println(f.getPath());
        String result = null;

        if(f.exists() && f.canRead()) {
            try {
                result = tesseract.doOCR(f);
            } catch (TesseractException e) {
                result = e.getMessage();
            }
        } else {
            result = "not exist";
        }
        return result;
    }

    private Tesseract getTesseract() {

        Tesseract instance = new Tesseract();
        // 언어모델 경로
        instance.setDatapath(languagePath);
        instance.setLanguage("kor+eng+kor_vert");//"kor+eng"
        return instance;

    }


    private File multipartFileToFile(MultipartFile mfile) throws IOException {
        File f = new File(filePath + mfile.getOriginalFilename());
        mfile.transferTo(f);
        return f;
    }
}
