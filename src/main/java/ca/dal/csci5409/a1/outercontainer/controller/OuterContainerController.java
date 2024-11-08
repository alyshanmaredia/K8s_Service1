package ca.dal.csci5409.a1.outercontainer.controller;

import ca.dal.csci5409.a1.outercontainer.constant.Constants;
import ca.dal.csci5409.a1.outercontainer.model.FileRequest;
import ca.dal.csci5409.a1.outercontainer.service.FileHandlerService;
import ca.dal.csci5409.a1.outercontainer.model.Request;
import ca.dal.csci5409.a1.outercontainer.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@Slf4j
@RequestMapping(Constants.DEFAULT)
public class OuterContainerController {
    @Value("${spring.datasource.url}")
    private String processorService_BASEURL;
    @Value("${fileLocation}")
    private String filePath;

    @PostMapping(Constants.CALCULATEENDPOINT)
    public ResponseEntity<Response> FileHandler(@RequestBody Request request) throws Exception {
        String FullURL= processorService_BASEURL + Constants.PROCESSORSERVICE_COMPUTE_ENDPOINT;

        log.info("POST Request File Handler Controller with ProcessServiceURL:{}",FullURL);
        return FileHandlerService.GetProcessedDataResult(request,FullURL);
    }

    @PostMapping("test")
    public ResponseEntity<String> Test() throws Exception{
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping(Constants.STOREFILE)
    public ResponseEntity<Map<String,String>> storeFileData(@RequestBody FileRequest request) throws Exception {
        Map<String,String> result = FileHandlerService.CreateFile(request,filePath);
        return ResponseEntity.ok(result);
    }

}