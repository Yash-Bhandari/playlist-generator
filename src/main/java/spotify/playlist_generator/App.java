package spotify.playlist_generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.JsonArray;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.SavedTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

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
        Playlist p = api.createPlaylist("LegCheese", "auto1").build().execute();
        ArrayList<Track> songs = new ArrayList<Track>();
        for (int i = 0; i < 5100 / 50; i++) {
            System.out.println("Accessed " + i * 50 + " songs");
            for (SavedTrack s : api.getUsersSavedTracks().limit(50).offset(50*i).build().execute().getItems()) 
                songs.add(s.getTrack());
        }
        saveAll(songs);
        Collections.shuffle(songs);
        JsonArray songUris = new JsonArray(playlistSize); 
        for (int i = 0; i < playlistSize; i++) 
            songUris.add(songs.get(i));
        //api.addTracksToPlaylist(p.getId(), new JsonArray(100).add(element);)
        //AddTracksToPlaylistRequest add = api.addTracksToPlaylist
    }
    
    private static void saveAll(ArrayList<Track> songs) {
        String[] toSave = new String[songs.size()];
        for (int i = 0; i < songs.size(); i++) {
            StringBuilder toAdd = new StringBuilder();
            toAdd.append(songs.get(i).getName()).append(" | ");
            toAdd.append(songs.get(i).getDurationMs()).append(" | ");
            toAdd.append(songs.get(i).getUri()).append("\r\n");
            toSave[i] = toAdd.toString();
        }
        save(toSave, "Songs.txt");
    }
    
    private static <T> void save(T[] toSave, String fileName) {
        try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("SavedFiles/" + fileName)));
        for (int i = 0; i < toSave.length; i++) {
            writer.write (toSave[i].toString());
            if (i % 500 == 0) System.out.println("Saved " + i);
        }
        writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
