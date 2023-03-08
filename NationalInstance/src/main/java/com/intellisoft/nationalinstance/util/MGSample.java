package com.intellisoft.nationalinstance.util;

import java.io.File;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
//public class MGSample {
//    // ...
//    public static JsonNode sendSimpleMessage() throws UnirestException {
//        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + YOUR_DOMAIN_NAME + "/messages")
//			.basicAuth("api", "66fca56036b7578dcd9c42c84a3e8be3-7764770b-af2354a8")
//                .queryString("from", "Excited User <USER@YOURDOMAIN.COM>")
//                .queryString("to", "artemis@example.com")
//                .queryString("subject", "hello")
//                .queryString("text", "testing")
//                .asJson();
//        return request.getBody();
//    }
//}
