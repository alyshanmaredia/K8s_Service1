package ca.dal.csci5409.a1.outercontainer.controller;

import ca.dal.csci5409.a1.outercontainer.constant.Constants;
import ca.dal.csci5409.a1.outercontainer.service.FileHandlerService;
import ca.dal.csci5409.a1.outercontainer.model.Request;
import ca.dal.csci5409.a1.outercontainer.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping(Constants.DEFAULT)
public class OuterContainerController {
    @Value("${spring.datasource.url}")
    private String processorService_BASEURL;
    @PostMapping(Constants.CALCULATEENDPOINT)
    public ResponseEntity<Response> FileHandler(@RequestBody Request request) throws Exception {
        String FullURL= processorService_BASEURL + Constants.PROCESSORSERVICE_COMPUTE_ENDPOINT;

        log.info("POST Request File Handler Controller with ProcessServiceURL:{}",FullURL);
        return FileHandlerService.GetProcessedDataResult(request,FullURL);
    }
}