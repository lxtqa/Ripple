package ripple.server.simulation.helper;

import brave.Tracing;
import ripple.server.simulation.core.tracing.brave.httpasyncclient.TracingHttpAsyncClientBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author songao
 *
 * 基于apache common async http client实现的的异步http客户端
 * 能够进行请求追踪
 * */
public class TracingHttpAsyncClientHelper {
    public static void asyncTracingHttpPostLarge(String url, List<String> headers, String content
            , Tracing tracing
            , FutureCallback<HttpResponse> callback) {

        final HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json; charset=UTF-8");
        post.setHeader("Accept-Charset", "UTF-8");
        post.setHeader("Accept-Encoding", "gzip");
        post.setHeader("Content-Encoding", "gzip");
        if (!CollectionUtils.isEmpty(headers)) {
            for (String header : headers) {
                post.setHeader(header.split("=")[0], header.split("=")[1]);
            }
        }

        post.setEntity(new ByteArrayEntity(content.getBytes(StandardCharsets.UTF_8)));
        CloseableHttpAsyncClient client = TracingHttpAsyncClientBuilder.create(tracing).build();
        client.start();
        client.execute(post,callback);
    }
}
