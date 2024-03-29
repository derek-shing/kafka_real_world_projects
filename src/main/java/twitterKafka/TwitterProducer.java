package twitterKafka;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {
    public static void main(String[] args) {
        new TwitterProducer().run();

    }

    public void run() {

        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100);
        Client client = createTwitterClient(msgQueue);


        client.connect();

        while (!client.isDone()) {
            String msg = null;
            try {
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }
            System.out.println(msg);


        }


        



    }

    public Client createTwitterClient(BlockingQueue<String> msgQueue){

        Dotenv dotenv = Dotenv.load();
        String consumerKey = dotenv.get("consumerKey");
        String consumerSecret = dotenv.get("consumerSecret");
        String token = dotenv.get("token");
        String secret = dotenv.get("secret");


        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        // Optional: set up some followings and track terms
        //List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList("Donald Trump");
        //hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);
        Authentication hosebirdAuth;
        hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);
        // These secrets should be read from a config file

        ClientBuilder builder = new ClientBuilder()
          .name("Hosebird-Client-01")                              // optional: mainly for the logs
          .hosts(hosebirdHosts)
          .authentication(hosebirdAuth)
          .endpoint(hosebirdEndpoint)
          .processor(new StringDelimitedProcessor(msgQueue));

        Client hosebirdClient = builder.build();


    return hosebirdClient;

            // Attempts to establish a connection.

        //hosebirdClient.connect();





    }
}
