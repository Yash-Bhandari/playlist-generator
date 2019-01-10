package spotify.playlist_generator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import authorization.Authenticator;
import fi.iki.elonen.NanoHTTPD;


public class Server extends NanoHTTPD{
    
    public Server(int port) throws IOException{
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:" +  port + "/ \n");
    }
    
   @Override
   public Response serve(IHTTPSession session) {
       String msg = "<html><body><h1>Creating a playlist for you</h1>\n";
       Map<String, String> parms = session.getParms();
       if (parms.get("username") == null) {
           //msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
           //msg += "<a href = " + Authenticator.
       } else {
           msg += "<p>Hello, " + parms.get("username") + "!</p>";
       }
       return newFixedLengthResponse(msg + "</body></html>\n");
   }
   
   public void printParms() {
   }

}
