import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

public class Main {

    private static final String pathToImage = "/tmp/tempImg.jpg"; // PATH FOR USE WITH AWS
//    private static final String pathToImage = "tmp\\tempImg.jpg"; // PATH FOR LOCAL TESTING, NEED TO MANUALLY ADD \tmp\

    public static void main() {
        Today today = new Today();
        String tweetBody = getTweetBody(today);
        saveImage(today.getAlbumArtURL());
        TwitterPoster.postToTwitter(tweetBody, pathToImage);
        today.updatePlaylist();
        System.out.println("\n--------\nEnd of Main.main");
    }

    private static String getTweetBody(Today info) {
        LocalDate date = LocalDate.now();
        DayOfWeek dow = date.getDayOfWeek();

        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        String dayOfMonthStr = String.valueOf(dayOfMonth);
        String suffix;
        switch (dayOfMonthStr.charAt(dayOfMonthStr.length() - 1)) {
            case '1':
                suffix = "st";
                break;

            case '2':
                suffix = "nd";
                break;

            case '3':
                suffix = "rd";
                break;

            default:
                suffix = "th";
        }

        if (dayOfMonth>=10 && dayOfMonth<=20) {
            suffix = "th";
        }

        String[] monthName = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};
        String month = monthName[cal.get(Calendar.MONTH)];

        StringBuilder output = new StringBuilder();
        output.append("It's " + dow.getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " the ");
        output.append(dayOfMonthStr + suffix + " of " + month + "!\n");
        output.append("Today's song is " + info.getTrackName() + " by " + info.getArtistsNamesString() + ".");
        output.append("\nGo listen to it (and all the others!) here: http://bit.ly/tunes_of_lew");
        return output.toString();
    }

    private static void saveImage(String urlStr) {
        try {
            BufferedImage image = null;

            URL url = new URL(urlStr);
            image = ImageIO.read(url);

            ImageIO.write(image, "jpg", new File(pathToImage));
        } catch (Exception e) {
            System.err.println(e);
        }

    }
}