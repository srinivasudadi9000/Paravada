package com.example.adimn.myapplication;

/**
 * Created by Adimn on 25-05-2018.
 */

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by pari on 15-02-2018.
 */

public class JSONParser {
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    private static JSONObject jObject;
    private static String url;
    private static int method;
    private static List<NameValuePair> params;

    public static JSONObject makeServiceCall(String url, int method,
                                             List<NameValuePair> params) {
        JSONParser.url = url;
        JSONParser.method = method;
        JSONParser.params = params;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            // Checking http request method type
            System.out.println("method: " + method);
            if (method == POST) {
                System.gc();
                HttpPost httpPost = new HttpPost(url.trim());
                // adding post param
                System.out.println("params: " + params);
                System.out.println("url: " + url.trim());
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                }
                System.out.println("noty: " + httpPost);
                httpResponse = httpClient.execute(httpPost);
                System.out.println("STEP: 1 :" + httpResponse);
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                    System.out.println("STEP: 2 :" + url);

                }
                HttpGet httpGet = new HttpGet(url);
                httpResponse = httpClient.execute(httpGet);
            }
            else if (method == PUT) {
                System.gc();
                HttpPut httpPut = new HttpPut(url.trim());
                if (params != null) {
                    httpPut.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                }
                httpResponse = httpClient.execute(httpPut);
                System.out.println("STEP: 1 :" + httpResponse);
            }
            httpEntity = httpResponse.getEntity();
            System.out.println("STEP: 2 :" + httpEntity);
            // response = EntityUtils.toString(httpEntity);
            InputStream in = httpEntity.getContent(); // Get the data in the
            System.out.println("STEP: 3 :" + in); // entity
            String a = convertStreamToString(in);
            System.out.println("a:"+a);
            jObject = new JSONObject(a);
            System.out.println("jobject: " + jObject);

        } catch (JSONException je) {
            je.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jObject;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line.trim() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
