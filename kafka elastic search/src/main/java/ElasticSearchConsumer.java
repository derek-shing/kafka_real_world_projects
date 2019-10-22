import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class ElasticSearchConsumer {

    public static RestHighLevelClient createClient(){

        Dotenv dotenv= Dotenv.load();
        String elasticSearchID=dotenv.get("elasticSearchUserId");
        String elasticSearchpassword=dotenv.get("elasticSearchPassword");
        String host = "twitter-kafka-8285604266.us-west-2.bonsaisearch.net";


        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticSearchID,elasticSearchpassword));

        RestClientBuilder builder = RestClient.builder(new HttpHost(host,443,"https"));
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                        return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestHighLevelClient client = new RestHighLevelClient(builder);

        return client;

    }

    public static void main(String[] args) throws IOException {

        RestHighLevelClient client = createClient();

        String jsonString = "{\"foo\":\"bar\"}";

        IndexRequest indexRequest = new IndexRequest("twitter").source(jsonString, XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        String id = indexResponse.getId();
        System.out.println(id);






    }
}
