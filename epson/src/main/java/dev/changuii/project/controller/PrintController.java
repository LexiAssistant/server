package dev.changuii.project.controller;


import dev.changuii.project.service.PrintService;
import dev.changuii.project.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
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


    @GetMapping(value = "{email}/check/capability")
    public Mono<Boolean> checkDeviceCapability(@PathVariable(name = "email") String email) {
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
                                         @RequestParam MultipartFile file) throws IOException {
        log.info(uploadURL);
        log.info(file.getOriginalFilename());
        return printService.uploadFile(uploadURL,file);
    }

    @PostMapping(value = "/{email}/execute/print/{jobId}")
    public Mono<Boolean> executePrint(@PathVariable String jobId,
                                      @PathVariable String email)
    {
        return printService.executePrint(jobId,email);
    }


    @GetMapping(value = "/job/info/{jobId}/{email}")
    public Mono<Boolean> getJobInfo(@PathVariable String jobId,
                                   @PathVariable String email)
    {
        return printService.getPrintJobInfo(email, jobId);
    }




    @PostMapping("/test/{email}")
    public Mono<Boolean> test(@PathVariable("email") String email,
                              @RequestParam MultipartFile file) throws IOException {
        Mono<List<String>> res = printService.printSetting(email);
        log.info(res.toString());

        List<String> test = res.block();

        assert test != null;
        String jobId = test.get(0);
        String uploadURL = test.get(1);

        printService.uploadFile(uploadURL,file);

        log.info("\n jobID: " + jobId+ "\n uploadURL: " + uploadURL);

        log.info(printService.executePrint(jobId,email).toString());

        return printService.getPrintJobInfo(email, jobId);
    }





}
