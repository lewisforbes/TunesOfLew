import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class RefreshCreds {

    private static final String refreshToken = "<yours>";

    public static SpotifyApi refreshAccess(SpotifyApi api) {
        api.setRefreshToken(refreshToken);
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest;
        AuthorizationCodeCredentials authorizationCodeCredentials;
        try {
            authorizationCodeRefreshRequest = api.authorizationCodeRefresh().build();
            authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

            api.setAccessToken(authorizationCodeCredentials.getAccessToken());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.err.println("Unable to refresh tokens: " + e);
        }
        return api;
    }
}
