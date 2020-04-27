import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

public class ResetCredentials {

    private static final String clientId = "<yours>";
    private static final String clientSecret = "<yours>";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("https://lewisforbes.com/");

    private static SpotifyApi api = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).setRedirectUri(redirectUri).build();

    public static void main(String args[]) {
        api = makeNewCreds(api, mkCode());
        System.out.println("Refresh token: " + api.getRefreshToken());
    }

    private static String mkCode() {
        System.out.println("Click the following link:");
        printURI(api);
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the URL you were redirected to\n");
        String input = in.nextLine();
        return input.substring(input.indexOf('=')+1);
    }

    private static void printURI(SpotifyApi api) {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = api.authorizationCodeUri()
                .scope("playlist-modify-public")
                .show_dialog(false)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        System.out.println("URI: " + uri.toString());
    }

    private static SpotifyApi makeNewCreds(SpotifyApi api, String codeFromURI) {
        AuthorizationCodeRequest authorizationCodeRequest = api.authorizationCode(codeFromURI).build();
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            api.setAccessToken(authorizationCodeCredentials.getAccessToken());
            api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            return api;

//            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        throw new IllegalArgumentException("Was unable set credentials.");
    }
}
