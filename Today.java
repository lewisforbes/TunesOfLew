import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.albums.GetAlbumRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Today {
    private final SpotifyApi spotifyApi = PublicSpotifyCreds.getAPIWithToken();

    private final String addedTracksID = "76Vro891V9PIJLV6FrRxck";
    private final Track[] tracksAdded = getTracksFromPlayList(addedTracksID);

    private final Random rand = new Random();

    private Track chosenTrack;
    private String trackName;
    private ArrayList<String> artistsNames = new ArrayList<>();
    private String albumArtURL;


    public Today() {
        int attempts = 1;
        while (true) {
            this.chosenTrack = getTrackFromPlaylist(attempts-1, choosePlaylistID());
            if (isTrackUnique(chosenTrack)) {
                break;
            }
            attempts++;
        }

        this.trackName = chosenTrack.getName();
        for (ArtistSimplified as : chosenTrack.getArtists()) {
            this.artistsNames.add(as.getName());
        }
        this.albumArtURL = chosenTrack.getAlbum().getImages()[0].getUrl();
    }

    public String getTrackName() {
        return this.trackName;
    }

    public String[] getArtistsNames() {
        String[] output = new String[artistsNames.size()];
        for (int i=0; i<output.length; i++) {
            output[i] = artistsNames.get(i);
        }
        return output;
    }

    public String getArtistsNamesString() {
        String[] artistsNames = getArtistsNames();
        String output = artistsNames[0];
        if (artistsNames.length == 1) {
            return output;
        }
        for (int i=1; i<artistsNames.length-1; i++) {
            output += ", " + artistsNames[i];
        }
        output += " and " + artistsNames[artistsNames.length-1];
        return output;
    }

    public String getAlbumArtURL() {
        return albumArtURL;
    }


    public void updatePlaylist() {
        AddToPlaylist.addToPlaylist(spotifyApi, chosenTrack.getUri());
    }


    private boolean isTrackUnique(Track inputTrack) {
        for (Track addedTrack : tracksAdded) {
            if (addedTrack.getId().equals(inputTrack.getId())) {
                return false;
            }
        }
        return true;
    }

    private Track[] getTracksFromPlayList(String playlistID) {
        GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(playlistID).additionalTypes("track,episode").build();

        try {
            final Playlist playlist = getPlaylistRequest.execute();
            PlaylistTrack[] plTraks = playlist.getTracks().getItems();
            Track[] output = new Track[plTraks.length];
            for (int i=0; i<output.length; i++) {
                output[i] = (Track) plTraks[i].getTrack();
            }
            return output;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        throw new IllegalArgumentException("Was unable to get tracks from playlist.");
    }

    private Track getTrackFromPlaylist(int trackIndex, String playlistID) {
        try {
            return getTracksFromPlayList(playlistID)[trackIndex];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private String choosePlaylistID() {
        final ArrayList<String> playlistIDs = new ArrayList<>(Arrays.asList(
                "37i9dQZF1E35BpvnMmO59L", // Daily Mix 1
                "37i9dQZF1E362JpMcmH2iO")); // Daily Mix 2
        return playlistIDs.get(rand.nextInt(playlistIDs.size()));
    }

    private Album getAlbum(String albumID) {
        final GetAlbumRequest getAlbumRequest = spotifyApi.getAlbum(albumID).build();
        Album output = null;
        try {
            output = getAlbumRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return output;
    }

    private Playlist getPlaylist(String playlistID) {
        final GetPlaylistRequest getPlaylistRequest = spotifyApi.getPlaylist(playlistID).build();
        Playlist output = null;
        try {
            output = getPlaylistRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return output;
    }
}