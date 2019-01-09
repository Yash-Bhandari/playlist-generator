package spotify.playlist_generator;

import java.io.IOException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

import authorization.Authenticator;

public class App {

    public static void main(String[] args) throws Exception{
        try {
            Server server = new Server(8080);
        } catch (IOException e) {
            System.out.println("didn't make a server");
        }
        new Authenticator(8080);
        //Authenticator.authorizationCodeUri_Sync();
        //execute();
    }
    
    /*static void execute () throws Exception{
        Authenticator.authorizationCode_Sync();
        SpotifyApi api = Authenticator.getApi();
        GetUsersCurrentlyPlayingTrackRequest out = api.getUsersCurrentlyPlayingTrack().build();
        CurrentlyPlaying c = out.execute();
        System.out.println(c.getItem().getName());
    }*/

}
