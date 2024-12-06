package com.example.mytunes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;

public class MyTunesController
{
    @FXML
    private VBox playlistVbox;
    @FXML
    private AnchorPane centerView;

    @FXML
    private Label songTitleLabel;
    @FXML
    private Label songArtistLabel;
    @FXML
    private Label currentTimeLabel;
    @FXML
    private Slider progressBar;

    MediaPlayer mediaPlayer = new MediaPlayer(this);

    Library library = new Library();

    Logger logger = Logger.getLogger(MyTunesController.class.getName());

    Playlist selectedPlaylist;



    @FXML
    private void addPlaylist()
    {
        try {
            library.newPlaylist();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playlist-item.fxml"));

            HBox playlist = loader.load();
            playlistVbox.getChildren().add(playlist);
            PlaylistItemController controller = loader.getController();
            controller.setMyTunesController(this);

            Playlist playList = library.newPlaylist();
            controller.setPlaylist(playList);

            playList.setPlayListController(controller);

            playlist.setUserData(controller);
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    public void setPlaylistTitle(Playlist playlist)
    {
        // Print the UUID of the playlist being passed in
        logger.info("Attempting to update playlist with UUID: " + playlist.getUuid());

        for (Node node : playlistVbox.getChildren()) {
            if (node instanceof HBox) {
                // Get the PlaylistItemController from userData
                PlaylistItemController itemController = (PlaylistItemController) node.getUserData();

                // Check if the controller is not null
                if (itemController != null) {
                    // Debug print for the UUID of the playlist in the sidebar
                    logger.info("Sidebar playlist UUID: " + itemController.getPlaylist().getUuid());

                    // Compare UUIDs instead of playlist names
                    if (itemController.getPlaylist().getUuid().equals(playlist.getUuid())) {
                        logger.info("UUIDs match! Updating playlist name...");
                        itemController.updatePlaylistTitleUI();
                        break;
                    }
                } else {
                    logger.info("Item controller is null for this node.");
                }
            }
        }
    }

    public void onPlaylistSelected(Playlist playlist)
    {
        if (playlist != null){
            switchToPlaylist(playlist);
        }
    }

    private void switchToPlaylist(Playlist playlist)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playlist-view.fxml"));
            BorderPane newView = loader.load();

            PlaylistViewController controller = loader.getController(); // Get PlaylistViewController
            controller.setPlaylist(playlist); // Set the selected playlist in the PlaylistViewController
            controller.setLibrary(library);// Optionally, pass the user library to the playlist view
            controller.setMyTunesController(this);
            controller.initializeCustom();

            selectedPlaylist = playlist;

            centerView.getChildren().add(newView); // Set the new view in the center of the BorderPane
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupUserDocuments()
    {

    }

    @FXML
    private void importSong() {

        // create a File chooser
        FileChooser fil_chooser = new FileChooser();

        // Make a filter of which file types the user can import
        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.FLAC", "*.MP3", "*.WAV"));

        // Show to file chooser dialog window
        File selectedFile = fil_chooser.showOpenDialog(centerView.getScene().getWindow());

        // Did the user select a file?
        if (selectedFile != null) {
            // Create a new songParser object
            SongFinder songFinder = new SongFinder();

            // Grab all the metadata from the song file
            Song newSong = songFinder.findSong(selectedFile);

            // Add the song to the users library
            library.addSong(newSong);

            // Log
            logger.info("Added song: " + library.getSongs().getLast());

            // If this album doesn't exist in the users library, create it here
            if (!library.doesAlbumExist(newSong.getAlbumTitle())) {
                // If the album doesn't exist, create a new one
                library.createNewAlbum(newSong);

            } else if (library.doesAlbumExist(newSong.getAlbumTitle())) {
                // If the imported song comes from the same album, then add it to the album
                Album album = library.findAlbum(newSong.getAlbumTitle());
                album.addSongToAlbum(newSong);
            }
        }
    }

    // Update the song UI with current song information
    public void updateSongUI(Song song) {
        if (song == null) {
            // Clear UI if no song is selected
            songTitleLabel.setText("No song playing");
            songArtistLabel.setText("");
            currentTimeLabel.setText("0:00");
            //progressBar.setProgress(0);
        } else {
            // Update UI with song details
            songTitleLabel.setText(song.getSongTitle());
            songArtistLabel.setText(song.getSongArtist());
            currentTimeLabel.setText("0:00");
            //progressBar.setProgress(0);
        }
    }

    @FXML
    private void toggleSong() {
        /*
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pauseSong();
            } else {
                mediaPlayer.resumeSong();
            }
        } else {
            logger.warning("No song is currently loaded.");
        }*/

        mediaPlayer.doPlaySongInPlaylist(library.getSongs().getLast());
    }
}