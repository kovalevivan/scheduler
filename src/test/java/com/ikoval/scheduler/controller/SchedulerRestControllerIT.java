package com.ikoval.scheduler.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
public class SchedulerRestControllerIT {

    private static final String TEST_REQUEST_FILE_PATH =  "src/test/resource/test_request.json";
    private static final String TEST_RESPONSE_FILE_PATH =  "src/test/resource/test_response.json";

    private String testRequestString;
    private String testResponseString;

    @Before
    public void setup() throws IOException {
        testRequestString = new String(Files.readAllBytes(Paths.get(TEST_REQUEST_FILE_PATH)));
        testResponseString = new String(Files.readAllBytes(Paths.get(TEST_RESPONSE_FILE_PATH)));
    }

    @Test
    public void givenTestRequestString_whenSubmitReservation_thanTestResponseReturned() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(testRequestString,headers);

        String fooResourceUrl
                = "http://localhost:8080/api/v1/reserve";
        System.out.println(testRequestString);
        ResponseEntity<String> response
                = restTemplate.postForEntity(fooResourceUrl,request,String.class,headers);

        assertEquals(testResponseString,response.getBody());
    }
}
