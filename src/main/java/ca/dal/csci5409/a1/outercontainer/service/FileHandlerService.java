package ca.dal.csci5409.a1.outercontainer.service;

import ca.dal.csci5409.a1.outercontainer.constant.Constants;
import ca.dal.csci5409.a1.outercontainer.exception.CustomException;
import ca.dal.csci5409.a1.outercontainer.model.FileRequest;
import ca.dal.csci5409.a1.outercontainer.model.Request;
import ca.dal.csci5409.a1.outercontainer.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.*;
import java.util.Objects;

@Slf4j
public class FileHandlerService {

    public static ResponseEntity<Response> GetProcessedDataResult(Request req,String processorServiceURL) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        log.info("[{}] Starting data processing", methodName);
        CheckRequestFile(req);
        CheckDataSourceFileExists(req);
        log.info("Processor Container URL: [{}]",processorServiceURL);
        ResponseEntity<Response> responseEntity = CallProcessorContainer(req, processorServiceURL);
        log.info("[{}] Data processing completed", methodName);

        return responseEntity;
    }

    private static void CheckRequestFile(Request req) throws CustomException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        if (Objects.isNull(req.getFile())) {
            log.error("[{}] File name is 'Null'", methodName);
            throw new CustomException(HttpStatus.BAD_REQUEST, Constants.INVALIDJSON, null);
        }
        log.info("[{}] File Name Exists: {}", methodName, req.getFile());
    }

    private static <InputStream> void CheckDataSourceFileExists(Request req) throws CustomException {
        String fileName = req.getFile();
        String fullFilePath = Constants.DATASOURCEPATH + fileName;
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        try {
            log.info("[{}] Checking if datasource file exists in outercontainer: {}", methodName, fullFilePath);
            InputStream in = (InputStream) new FileInputStream(fullFilePath);
            log.info("[{}] Datasource file found: {}", methodName, fullFilePath);
        }
        catch (FileNotFoundException exception) {
            log.error("[{}] File not found at path: {}. Exception: {}", methodName, fullFilePath, exception.getMessage());
            throw new CustomException(HttpStatus.NOT_FOUND, Constants.FILENOTFOUND, fileName);
        }
    }

    private static ResponseEntity<Response> CallProcessorContainer(Request request, String uri) throws Exception {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        try {
            log.info("[{}] Method to call Processor Container with URI: {}", methodName, uri);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Request> requestEntity = new HttpEntity<>(request);
            ResponseEntity<Response> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
            log.info("[{}] Successfully called Processor Container", methodName);
            return response;
        }
        catch (ResourceAccessException exception) {
            log.error("[{}] Resource Access Exception: {}", methodName, exception.getMessage());
            throw new CustomException(HttpStatus.NOT_FOUND, request.getFile(), Constants.SECONDSERVICEUNAVAILABLE);
        }
        catch (Exception exception) {
            String errorMessage = ExtractErrorMessage(exception);
            log.error("[{}] Exception Error Message: {}", methodName, exception.getMessage());
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, request.getFile());
        }
    }

    private static String ExtractErrorMessage(Exception exception) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("[{}] Extracting Exception Error Message", methodName);

        String message = exception.getMessage();

        String errorKey = "\"error\":\"";
        int errorStartIndex = message.indexOf(errorKey) + errorKey.length();
        int errorEndIndex = message.indexOf('"', errorStartIndex);

        if (errorStartIndex > errorKey.length() - 1 && errorEndIndex > errorStartIndex) {
            return message.substring(errorStartIndex, errorEndIndex);
        }

        log.info("[{}] Extracted error message : [{}]", methodName, message);
        return message;
    }

    public static Map<String, String> CreateFile(FileRequest request, String filePath) {

        if(Objects.isNull(request.getFile())){
            Map<String,String> errorInfo = new LinkedHashMap<>();
            errorInfo.put("file",null);
            errorInfo.put("error","Invalid JSON input.");
            return errorInfo;
        }

        String data = request.getData();
        String fn = request.getFile();

        try {
            StringBuilder stringBuilder = new StringBuilder();
            FileWriter fileWriter = new FileWriter(filePath + fn);
            String[] records = data.split("\n");

            for(String r: records){
                String[] object = r.split(",");
                stringBuilder.append(object[0].trim()).append(",").append(object[1].trim()).append("\n");
            }

            fileWriter.write(stringBuilder.toString());
            fileWriter.close();

            Map<String, String> successResponse = new HashMap<>();

            successResponse.put("file", request.getFile());
            successResponse.put("message", "Success.");


            return successResponse;
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();

            errorResponse.put("file", request.getFile());
            errorResponse.put("error", "Error while storing the file to the storage.");

            return errorResponse;
        }
    }
}
