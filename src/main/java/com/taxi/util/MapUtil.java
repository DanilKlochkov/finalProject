package com.taxi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class MapUtil {
    private RestTemplate restTemplate;

    public String toGeoCode(String address) throws IOException {
        restTemplate = new RestTemplate();
        var url = String.format("https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=7eb77f56131f4b33a60a56c087af7813", address);
        ResponseEntity<String> userData = restTemplate.getForEntity(url, String.class);
        var mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(userData.getBody());
        return node.get("features").get(0).get("properties").get("lat").toString() + "," + node.get("features").get(0).get("properties").get("lon").toString();
    }

    public String toAddress(String geoCode) throws JsonProcessingException {
        var geoCodesArray = geoCode.split(",");
        restTemplate = new RestTemplate();
        var url = String.format("https://api.geoapify.com/v1/geocode/reverse?lat=%s&lon=%s&format=json&apiKey=7eb77f56131f4b33a60a56c087af7813", geoCodesArray[0], geoCodesArray[1]);
        ResponseEntity<String> userData = restTemplate.getForEntity(url, String.class);
        var mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(userData.getBody());
        return node.get("results").get(0).get("formatted").toString();
    }

    public String getDistance(String geoCode) throws IOException {
        restTemplate = new RestTemplate();
        var url = String.format("https://api.geoapify.com/v1/routing?waypoints=%s&format=json&mode=drive&details=instruction_details&apiKey=7eb77f56131f4b33a60a56c087af7813", geoCode);
        ResponseEntity<String> userData = restTemplate.getForEntity(url, String.class);
        var mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(userData.getBody());
        return node.get("results").get(0).get("distance").toString();
    }
}