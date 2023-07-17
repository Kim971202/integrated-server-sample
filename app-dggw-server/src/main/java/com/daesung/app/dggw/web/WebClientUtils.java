package com.daesung.app.dggw.web;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.netty.http.client.HttpClient;

import java.lang.reflect.Field;

@Slf4j
public class WebClientUtils {

    public static UriSpec<RequestBodySpec> getSslClient(String baseUrl, String contentType, HttpMethod method) {
        log.info("getSslClient(String baseUrl, String contentType, HttpMethod method) called");

        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            UriSpec<RequestBodySpec> uriSpec = WebClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build()
                    .method(method);

            return uriSpec;
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public static UriSpec<RequestBodySpec> getSslClient(String baseUrl, String contentType, HttpMethod method, String token) {
        log.info("getSslClient(String baseUrl, String contentType, HttpMethod method, String token) called");

        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            UriSpec<RequestBodySpec> uriSpec = WebClient.builder()
                    .baseUrl(baseUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, contentType)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build()
                    .method(method);

            return uriSpec;
        } catch (Exception e) {
            log.error("", e);
        }

        return null;
    }

    public static UriSpec<RequestBodySpec> getQueryClient(String baseUrl, HttpMethod method) {
        log.info("getQueryClient(String baseUrl, HttpMethod method)  called");

        try {
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            UriSpec<RequestBodySpec> uriSpec = WebClient.builder()
                    .baseUrl(baseUrl)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build()
                    .method(method);

            return uriSpec;
        } catch (Exception e) {
            log.error("", e);
        }

        return null;
    }

    public static String getRequestString(Class<?> clazz, Object object) {
        log.info("getRequestString(Class<?> clazz, Object object) called");

        String queryString = "";

        try {
            Class<?> superClazz = clazz.getSuperclass();

            if (superClazz != null) {
                for (Field f : superClazz.getDeclaredFields()) {
                    f.setAccessible(true);

                    Object fieldValue = f.get(object);
                    if (fieldValue != null) {
                        queryString += f.getName() + "=" + String.valueOf(fieldValue) + "&";
                    }
                }
            }

            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);

                Object fieldValue = f.get(object);
                if (fieldValue != null) {
                    queryString += f.getName() + "=" + String.valueOf(fieldValue) + "&";
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }

        queryString = queryString.substring(0, queryString.length() - 1);
        log.debug("queryString:{}", queryString);

        return queryString;
    }

    public static MultiValueMap<String, String> getRequestFormData(Class<?> clazz, Object object) {

        log.info("getRequestFormData(Class<?> clazz, Object object) called");


        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        try {
            Class<?> superClazz = clazz.getSuperclass();

            if (superClazz != null) {
                for (Field f : superClazz.getDeclaredFields()) {
                    f.setAccessible(true);

                    Object fieldValue = f.get(object);
                    if (fieldValue != null) {
                        formData.add(f.getName(), String.valueOf(fieldValue));
                    }
                }
            }

            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);

                Object fieldValue = f.get(object);
                if (fieldValue != null) {
                    formData.add(f.getName(), String.valueOf(fieldValue));
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }

        log.debug("formData:{}", formData.toString());

        return formData;
    }
}