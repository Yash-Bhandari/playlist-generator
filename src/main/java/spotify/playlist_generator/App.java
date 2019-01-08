package spotify.playlist_generator;

import java.net.URI;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

/**
 * Hello world!
 *
 */
public class App {

	private static final SpotifyApi api = new SpotifyApi.Builder().setClientId("dfaef265d1624a78810ee848076a1eb6")
			.setClientSecret("a7600d7a65b843278af6294571e80bca").build();
	
	private static final AuthorizationCodeUriRequest request = api.authorizationCodeUri().show_dialog(false).build();

	public static void main(String[] args) {
		final URI uri = request.execute();

	    System.out.println("URI: " + uri.toString());
	}
}
