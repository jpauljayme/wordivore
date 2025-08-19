package dev.jp.wordivore.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ClientLoggerRequestInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(ClientLoggerRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
     log.info("Making {} request to {}", request.getMethod(), request.getURI());
     return execution.execute(request, body);
    }

//    private void logRequest(HttpRequest request, byte[] body){
//        log.info("Request: {} {}", request.getMethod(), request.getURI());
//    }
//
//    private ClientHttpResponse logResponse(HttpRequest request, ClientHttpResponse response) throws  IOException{
//        log.info("Response Status : {}", response.getStatusCode());
//
//        byte[] responseBody =  response.getBody().readAllBytes();
//
//        if(responseBody.length > 0){
//            log.info("Response Body: {}", new String(responseBody, StandardCharsets.UTF_8));
//        }
//
//        return new BufferingClientHttpRequestFactory(response, responseBody);
//    }
//
////    private void logHeaders(HttpHeaders httpHeaders){
////        httpHeaders.forEach( (name, values) -> );
////    }
}
