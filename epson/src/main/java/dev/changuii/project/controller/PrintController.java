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
    public Mono<Boolean> executePrint(@PathVariable("jobId") String jobId,
                                      @PathVariable("email") String email)
    {
        log.info("=-============== executePrint");
        log.info(jobId);
        log.info(email);
        return printService.executePrint(email,jobId);
    }


    @GetMapping(value = "/job/info/{jobId}/{email}")
    public Mono<Boolean> getJobInfo(@PathVariable("jobId") String jobId,
                                   @PathVariable("email") String email)
    {
        return printService.getPrintJobInfo(email, jobId);
    }




    @PostMapping("/test/{email}")
    public Mono<Boolean> test(@PathVariable("email") String email,
                              @RequestParam("file") MultipartFile file) throws IOException {
        String jobId = "";
        String url = "";
        List<String> data = printService.printSetting(email).block();
        jobId = data.get(0);
        url = data.get(1);

        log.info(jobId);
        return printService.uploadFile(url, file);

//        return printService.executePrint(email, jobId);


    }







}
