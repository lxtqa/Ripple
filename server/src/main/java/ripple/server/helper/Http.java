package ripple.server.helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Zhen Tang
 */
public final class Http {
    private Http() {

    }

    public static String get(String url, Map<String, String> headers) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(url);
            for (String name : headers.keySet()) {
                httpGet.setHeader(name, headers.get(name));
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String body = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(httpEntity);
            return body;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String post(String url, Map<String, String> headers) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            for (String name : headers.keySet()) {
                httpPost.setHeader(name, headers.get(name));
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String body = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(httpEntity);
            return body;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String post(String url, Map<String, String> headers, String requestBody, ContentType contentType) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            for (String name : headers.keySet()) {
                httpPost.setHeader(name, headers.get(name));
            }
            StringEntity entity = new StringEntity(requestBody, contentType);
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String body = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(httpEntity);
            return body;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String postJson(String url, String requestBody) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String body = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(httpEntity);
            return body;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String delete(String url, Map<String, String> headers) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpDelete httpDelete = new HttpDelete(url);
            for (String name : headers.keySet()) {
                httpDelete.setHeader(name, headers.get(name));
            }
            HttpResponse httpResponse = httpClient.execute(httpDelete);
            HttpEntity httpEntity = httpResponse.getEntity();
            String body = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
            EntityUtils.consume(httpEntity);
            return body;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}