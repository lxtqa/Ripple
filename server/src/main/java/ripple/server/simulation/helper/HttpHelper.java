package ripple.server.simulation.helper;

import brave.Tracing;
import ripple.server.simulation.core.tracing.brave.httpclient.TracingHttpClientBuilder;
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author fuxiao.tz
 */

public class HttpHelper {
    private static String doGet(String url, Map<String, String> headers, HttpClient httpClient) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        for (String name : headers.keySet()) {
            httpGet.setHeader(name, headers.get(name));
        }
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        String body = EntityUtils.toString(httpEntity, Charset.forName("utf-8"));
        EntityUtils.consume(httpEntity);
        return body;
    }

    private static String doPost(String url, Map<String, String> headers, String requestBody, ContentType contentType, HttpClient httpClient) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        for (String name : headers.keySet()) {
            httpPost.setHeader(name, headers.get(name));
        }
        StringEntity entity = new StringEntity(requestBody, contentType);
        httpPost.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String body = EntityUtils.toString(httpEntity, Charset.forName("utf-8"));
        EntityUtils.consume(httpEntity);
        return body;
    }

    public static String get(String url, Map<String, String> headers) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            return doGet(url, headers, httpClient);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String get(String url, Map<String, String> headers, Tracing tracing) {
        try {
            HttpClient httpClient = TracingHttpClientBuilder.create(tracing).build();
            return doGet(url, headers, httpClient);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String post(String url, Map<String, String> headers, String requestBody, ContentType contentType) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            return doPost(url, headers, requestBody, contentType, httpClient);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String post(String url, Map<String, String> headers, String requestBody, ContentType contentType, Tracing tracing) {
        try {
            HttpClient httpClient = TracingHttpClientBuilder.create(tracing).build();
            return doPost(url, headers, requestBody, contentType, httpClient);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String postJson(String url, Map<String, String> headers, String requestBody) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            return doPost(url, headers, requestBody, ContentType.APPLICATION_JSON, httpClient);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static String postJson(String url, Map<String, String> headers, String requestBody, Tracing tracing) {
        try {
            HttpClient httpClient = TracingHttpClientBuilder.create(tracing).build();
            return doPost(url, headers, requestBody, ContentType.APPLICATION_JSON, httpClient);
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
            String body = EntityUtils.toString(httpEntity, Charset.forName("utf-8"));
            EntityUtils.consume(httpEntity);
            return body;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}