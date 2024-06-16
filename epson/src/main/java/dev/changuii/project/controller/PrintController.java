package dev.changuii.project.controller;


import dev.changuii.project.service.PrintService;
import dev.changuii.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;


@RestController
@RequestMapping(value = "/api/print")
public class PrintController {

    Logger log = LoggerFactory.getLogger(PrintController.class);

    private final PrintService printService;
    private final UserService userService;

    public PrintController(@Autowired PrintService printService,
                           @Autowired UserService userService) {
        this.printService = printService;
        this.userService = userService;
    }


    @PostMapping(value = "{email}/check/capability/{printerEmail}")
    public Mono<Boolean> checkDeviceCapability(@PathVariable(name = "email") String email,
                                      @PathVariable(name = "printerEmail") String printerEmail) {
        return printService.getDevicePrintCapability(email);
    }


    @PostMapping(value = "/setting/{email}")
    public Mono<List<String>> printerSetting(@PathVariable(value = "email") String email)
    {
        Mono<List<String>> res = printService.printSetting(email);
        log.info(res.toString());
        return res;
    }

    @PostMapping(value = "/file/upload")
    public Mono<Boolean> printFileUpload(@RequestParam String uploadURL,
                                         @RequestPart MultipartFile file) {
        log.info(uploadURL);
        log.info(file.getOriginalFilename());
        return printService.uploadFile(uploadURL,file);
    }




}
