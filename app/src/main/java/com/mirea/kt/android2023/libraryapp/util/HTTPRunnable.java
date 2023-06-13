package com.mirea.kt.android2023.libraryapp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class HTTPRunnable implements Runnable {

    private String address;
    private HashMap<String, String> requestBody;
    private String responseBody;

    public HTTPRunnable(String address, HashMap<String, String> requestBody) {
        this.address = address;
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void run() {
        if (address != null) {
            try {
                URL url = new URL(address);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("POST");
                httpConnection.setDoOutput(true);
                OutputStreamWriter osw = new OutputStreamWriter(httpConnection.getOutputStream());
                osw.write(generateStringBody());
                osw.flush();
                int responseCode = httpConnection.getResponseCode();
                System.out.println("Response code: " + responseCode);
                if (responseCode == 200) {
                    InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream());
                    BufferedReader br = new BufferedReader(isr);
                    String currentLine;
                    StringBuilder sbResponse = new StringBuilder();
                    while ((currentLine = br.readLine()) != null) {
                        sbResponse.append(currentLine);
                    }
                    responseBody = sbResponse.toString();
                } else {
                    System.out.println("Error! Bad response code!");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String generateStringBody() {
        StringBuilder sbParams = new StringBuilder();
        if (requestBody != null && !requestBody.isEmpty()) {
            int i = 0;
            for (String key : requestBody.keySet()) {
                try {
                    if (i != 0) {
                        sbParams.append("&");
                    }
                    sbParams.append(key).append("=")
                            .append(URLEncoder.encode(requestBody.get(key), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    System.out.println(e.getMessage());
                }
                i++;
            }
        }
        System.out.println(sbParams.toString());
        return sbParams.toString();
    }
}
