package io.mobimenu.common.http;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;

public class WebClient {

    private final HttpClient client;

    public WebClient(Executor executor, int timeoutInSecs) {
        this.client = HttpClient.newBuilder()
                .executor(executor)
                .connectTimeout(Duration.ofSeconds(timeoutInSecs))
                .build();
    }

    public Uni<HttpResponse> get(String url, Map<String, String> headers)  {
        return Uni.createFrom().item(Unchecked.supplier(() -> new URI(url)))
                .map(uri -> {
                    var request = HttpRequest.newBuilder().uri(uri).GET();
                    headers.forEach(request::header);
                    return request.build();
                })
                .flatMap(request -> Uni.createFrom()
                    .completionStage(() -> client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
                    .thenApply((response) -> new HttpResponse(response.statusCode(), response.body()))));
    }

}
