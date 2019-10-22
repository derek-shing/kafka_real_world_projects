import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestHighLevelClient;

public class ElasticSearchConsumer {

    public static RestHighLevelClient createClient(){

        Dotenv dotenv= Dotenv.load();
        String elasticSearchID=dotenv.get("elasticSearchUserId");
        String elasticSearchpassword=dotenv.get("elasticSearchPassword");
        String host = "twitter-kafka-8285604266.us-west-2.bonsaisearch.net:443";

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticSearchID,elasticSearchpassword));






    }

    public static void main(String[] args) {

        createClient();




    }
}
