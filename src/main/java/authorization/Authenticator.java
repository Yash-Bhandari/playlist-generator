package authorization;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

public class Authenticator {
    private final int port;
    private final URI redirectUri;
    private final String clientId = "dfaef265d1624a78810ee848076a1eb6";
    private final String clientSecret = "a7600d7a65b843278af6294571e80bca";
    private final String scope = "user-read-currently-playing,user-read-currently-playing,playlist-modify-private,playlist-modify-public,user-library-read";
    private final SpotifyApi api;
    private final AuthorizationCodeRequest codeRequest;

    public Authenticator(int port) throws InterruptedException {
        this.port = port;
        redirectUri = SpotifyHttpManager.makeUri("http://localhost:" + port + "/");
        api = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret).setRedirectUri(redirectUri)
                .build();

        String authorizationCode = "";
        WebDriver driver = new ChromeDriver();
        driver.get(getRequestUrl());
        /*
         * driver.findElement((By.id("login-username"))).sendKeys("username");
         * driver.findElement((By.id("login-password"))).sendKeys("password");
         * driver.findElement(By.id("login-button")).click();
         * driver.findElement(By.id("auth-accept")).click();
         */
        while (!driver.getCurrentUrl().substring(0, redirectUri.toString().length()).equals(redirectUri.toString())) {
            Thread.sleep(1000);
        }

        System.out.println(driver.getCurrentUrl());
        String url = driver.getCurrentUrl();
        url = url.substring(url.indexOf('=') + 1);
        System.out.println(url);
        codeRequest = api.authorizationCode(url).build();
        authorizationCode_Sync();
    }

    // Gets the url the user is sent to to login to Spotify
    public String getRequestUrl() {
        final AuthorizationCodeUriRequest uriRequest = api.authorizationCodeUri().scope(scope).show_dialog(true)
                .build();
        final URI uri = uriRequest.execute();
        System.out.println("URI: " + uri.toString());
        return uri.toString();
    }

    public void authorizationCode_Sync() {
        try {
            final AuthorizationCodeCredentials credentials = codeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            api.setAccessToken(credentials.getAccessToken());
            api.setRefreshToken(credentials.getRefreshToken());

            System.out.println("Expires in: " + credentials.getExpiresIn());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /*
     * public void authorizationCodeUri_Async() { try { final Future<URI> uriFuture
     * = uriRequest.executeAsync(); final URI uri = uriFuture.get();
     * System.out.println("URI: " + uri.toString()); } catch (Exception e) {
     * System.out.println("Error: " + e.getCause().getMessage()); } }
     */

    public SpotifyApi getApi() {
        return api;
    }

    public void setRedirect(int port) {

    }
}
