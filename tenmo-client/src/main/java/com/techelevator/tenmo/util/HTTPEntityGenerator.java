package com.techelevator.tenmo.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class HTTPEntityGenerator {

    private static String token;

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static HttpEntity<String> generateAuthEntity() {
        return new HttpEntity<>(genAuthHead());
    }

    public static <T> HttpEntity<T> generateEntityWithBody(T body) {
        return new HttpEntity<>(body, genAuthHead());
    }

    public static HttpEntity<byte[]> generatedFileEntity(byte[] filecontent, String filename){
        HttpHeaders headers = genAuthHead();
        headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);
    }

    private static HttpHeaders genAuthHead(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return  headers;
    }
}
