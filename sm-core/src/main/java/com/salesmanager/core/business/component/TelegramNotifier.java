package com.salesmanager.core.business.component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class TelegramNotifier {

    private static final Logger logger = LoggerFactory.getLogger(TelegramNotifier.class);

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Value("${bot.addProductChatId}")
    private String addProductChatId;

    @Value("${bot.orderChatId}")
    private String orderChatId;

    RestTemplate restTemplate;

    //Long chatId = Long.valueOf(1143080199);


    public void sendmsgOnOrder(String msg) throws IOException, InterruptedException, URISyntaxException {
        sendmsg(msg, orderChatId);
    }

    public void sendmsgOnAddProduct(String msg) throws IOException, InterruptedException, URISyntaxException {
        sendmsg(msg, addProductChatId);
    }

    @Async
    public void sendmsg(String msg, String chatId) throws IOException, InterruptedException, URISyntaxException {

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();

        UriBuilder builder = UriBuilder
                .fromUri(new URI("https://api.telegram.org"))
                .path(String.format("/%s/sendMessage", token))
                .queryParam("chat_id", chatId)
                .queryParam("text", msg);
                //.queryParam("disable_web_page_preview", true)
                //.queryParam("entities", "url");


        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(builder.build())
                .timeout(Duration.ofSeconds(5))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        logger.info(response.toString());
    }

    @Async
    public void sendmsgOrder(String msg) throws IOException, InterruptedException, URISyntaxException {

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();

        UriBuilder builder = UriBuilder
                .fromUri(new URI("https://api.telegram.org"))
                .path(String.format("/%s/sendMessage", token))
                .queryParam("chat_id", addProductChatId)
                .queryParam("text", msg)
                .queryParam("disable_web_page_preview", true)
                .queryParam("entities", "url");


        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(builder.build())
                .timeout(Duration.ofSeconds(5))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        logger.info(response.toString());
    }
}
