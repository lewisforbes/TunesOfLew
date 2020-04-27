import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;

public class TwitterPoster {
    private static final String CONSUMER_KEY = "<yours>";
    private static final String SECRET_CONSUMER_KEY = "<yours>"";
    private static final String ACCESS_TOKEN = "<yours>";
    private static final String SECRET_ACCESS_TOKEN = "<yours>";


    public static void postToTwitter(String wordsInTweet, String pathToImage) {
        File pic = new File(pathToImage);
        Twitter twitter = configureTwitterCreds();
        try {
            UploadedMedia uploaded = twitter.uploadMedia(pic);
            StatusUpdate status = new StatusUpdate(wordsInTweet);
            status.setMedia(pic);
            Status actualStatus = twitter.updateStatus(status);
            System.out.println("The following tweet was posted successfully:\n" + actualStatus.getText());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static Twitter configureTwitterCreds() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(SECRET_CONSUMER_KEY)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(SECRET_ACCESS_TOKEN);
        TwitterFactory tf = new TwitterFactory(cb.build());
        return tf.getInstance();

    }


}
