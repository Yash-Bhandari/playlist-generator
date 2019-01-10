package spotify.playlist_generator;

import java.io.IOException;
import java.util.ArrayList;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.SavedTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;

import authorization.Authenticator;

public class App {

    public static void main(String[] args) throws Exception {
        initServer();

        SpotifyApi api = new Authenticator(8080).getApi();
        // printSong(api);
        createPlaylist(api, 5100, 100);
    }

    private static void initServer() {
        try {
            Server server = new Server(8080);
        } catch (IOException e) {
            System.out.println("didn't make a server");
        }
    }

    private static void printSong(SpotifyApi api) throws Exception {
        GetUsersCurrentlyPlayingTrackRequest out = api.getUsersCurrentlyPlayingTrack().build();
        CurrentlyPlaying c = out.execute();
        System.out.println(c.getItem().getName());

    }

    private static void createPlaylist(SpotifyApi api, int numSavedSongs, int playlistSize) throws Exception {
        CreatePlaylistRequest make = api.createPlaylist("LegCheese", "auto1").build();
        // Playlist p = make.execute();
        ArrayList<Track> songs = new ArrayList<Track>();
        for (int i = 0; i < 5100 / 50; i++) {
            System.out.println("Accessed " + i * 50 + " songs");
            for (SavedTrack s : api.getUsersSavedTracks().limit(50).offset(50*i).build().execute().getItems()) 
                songs.add(s.getTrack());
        }
        for (Track s : songs) {
            System.out.println(s.getName());
        }
        // AddTracksToPlaylistRequest add = api.addTracksToPlaylist(, uris)
    }
    
    

}
