package com.example.mytunes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyTunesApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the Library singleton
        Library.initializeLibrary();

        // Load the FXML file for the main UI and set the scene
        FXMLLoader fxmlLoader = new FXMLLoader(MyTunesApplication.class.getResource("myTunes-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("MyTunes");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        // Debugging to check data in the library before saving
        Library library = Library.getInstance(); // Use the singleton instance
        System.out.println("Checking data in library before saving:");
        System.out.println("Number of songs in the library: " + library.getSongs().size());
        System.out.println("Number of playlists in the library: " + library.getPlaylists().size());
        System.out.println("Number of albums in the library: " + library.getAlbums().size());

        // Optional: Loop through the elements to print detailed information
        library.getSongs().forEach(song -> System.out.println("Song: " + song.getSongTitle()));
        library.getPlaylists().forEach(playlist -> System.out.println("Playlist: " + playlist.getTitle()));
        library.getAlbums().forEach(album -> System.out.println("Album: " + album.getAlbumTitle()));

        // Save the library using the singleton instance
        DataMethod.saveLibrary(library);
    }
}