package spotify.playlist_generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
		createPlaylistsFromLocal(api, 200, 1, "SavedFiles/songs.txt");
		// createPlaylists(api, 200, 1, 5100);
		// printSong(api);
		// createPlaylist(api, 5100, 100);
		/*
		 * Playlist p = api.createPlaylist("LegCheese", "auto1").build().execute();
		 * System.out.println(p.getId()); String[] temp = {
		 * "spotify:track:7LRMbd3LEoV5wZJvXT1Lwb" }; api.addTracksToPlaylist(p.getId(),
		 * temp).build().execute();
		 */
	}

	private static void initServer() {
		try {
			Server server = new Server(8080);
		} catch (IOException e) {
			System.out.println("didn't make a server");
		}
	}

	public static void printSong(SpotifyApi api) throws Exception {
		GetUsersCurrentlyPlayingTrackRequest out = api.getUsersCurrentlyPlayingTrack().build();
		CurrentlyPlaying c = out.execute();
		System.out.println(c.getItem().getName());

	}

	public static void createPlaylists(SpotifyApi api, int playlistSize, int numPlaylists, int numSavedSongs)
			throws Exception {
		ArrayList<Track> songs = new ArrayList<Track>();
		for (int i = 0; i < numSavedSongs / 50; i++) {
			System.out.println("Accessed " + i * 50 + " songs");
			for (SavedTrack s : api.getUsersSavedTracks().limit(50).offset(50 * i).build().execute().getItems())
				songs.add(s.getTrack());
		}
		saveAll(songs);
		Collections.shuffle(songs);

		for (int j = 0; j < numPlaylists; j++) {
			if ((j + 1) * playlistSize > songs.size())
				break;
			Playlist p = api.createPlaylist("LegCheese", "auto" + (j + 1)).build().execute();
			int index = 0;
			while (index < playlistSize) {
				int batchSize = playlistSize - index >= 90 ? 90 : playlistSize - index; // 90 is the max number of songs
																						// that can be safely requested
																						// at a time
				String[] batch = new String[batchSize];
				for (int i = 0; i < batch.length; i++)
					batch[i] = songs.get(j * playlistSize + index + i).getUri();
				api.addTracksToPlaylist(p.getId(), batch).build().execute();
				index += batchSize;
			}
		}
	}

	public static void createPlaylistsFromLocal(SpotifyApi api, int playlistSize, int numPlaylists, String savePath)
			throws Exception {

		ArrayList<String> songs = new ArrayList<String>();
		for (String URI : readURIs(savePath))
			songs.add(URI);
		Collections.shuffle(songs);

		for (int j = 0; j < numPlaylists; j++) {
			if ((j + 1) * playlistSize > songs.size())
				break;
			Playlist p = api.createPlaylist("LegCheese", "auto" + (j + 1)).build().execute();
			int index = 0;
			while (index < playlistSize) {
				int batchSize = playlistSize - index >= 90 ? 90 : playlistSize - index;
				String[] batch = new String[batchSize];
				for (int i = 0; i < batch.length; i++)
					batch[i] = songs.get(j * playlistSize + index + i);
				api.addTracksToPlaylist(p.getId(), batch).build().execute();
				index += batchSize;
			}
		}
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
				writer.write(toSave[i].toString());
				if (i % 500 == 0)
					System.out.println("Saved " + i);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String[] readURIs(String path) {
		try {
			ArrayList<String> URIs = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
			String s;
			while ((s = reader.readLine()) != null) {
				URIs.add(s.substring(s.indexOf("spotify:track")));
			}
			reader.close();
			return URIs.toArray(new String[URIs.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
