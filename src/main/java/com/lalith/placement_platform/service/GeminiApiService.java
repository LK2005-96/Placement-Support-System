package com.lalith.placement_platform.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

@Service
public class GeminiApiService {

    @Value("${app.gemini.api-key}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build();
     private static final String GEMINI_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public String askGemini(String prompt) {
        JSONObject part = new JSONObject();
        part.put("text", prompt);

        JSONArray parts = new JSONArray();
        parts.put(part);

        JSONObject content = new JSONObject();
        content.put("parts", parts);

        JSONArray contents = new JSONArray();
        contents.put(content);

        JSONObject body = new JSONObject();
        body.put("contents", contents);

        RequestBody requestBody = RequestBody.create(
                body.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(GEMINI_URL + "?key=" + apiKey)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Gemini API error: " + response.code() + " - " + response.body().string());
            }
            String responseBody = response.body().string();
            JSONObject json = new JSONObject(responseBody);
            return json.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } catch (IOException e) {
            throw new RuntimeException("Failed to call Gemini API", e);
        }
    }
}