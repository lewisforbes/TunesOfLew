import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.special.SnapshotResult;
import com.wrapper.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class AddToPlaylist {
    private static final String playlistId = "76Vro891V9PIJLV6FrRxck";
    private static String[] uris = new String[1];

    private static AddItemsToPlaylistRequest addItemsToPlaylistRequest;

    private static void addItemsToPlaylist_Sync() {
        try {
            final SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addToPlaylist(SpotifyApi api, String trackToAdd) {
        uris[0] = trackToAdd;
        addItemsToPlaylistRequest = RefreshCreds.refreshAccess(api).addItemsToPlaylist(playlistId, uris).build();
        addItemsToPlaylist_Sync();
    }
}
