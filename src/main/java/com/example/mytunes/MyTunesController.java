package com.example.mytunes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class MyTunesController {
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

    @Getter
    private MediaPlayer mediaPlayer = new MediaPlayer(this);
    private Logger logger = Logger.getLogger(MyTunesController.class.getName());
    private Playlist selectedPlaylist;


    @FXML
    private void addPlaylist() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playlist-item.fxml"));

            HBox playlist = loader.load();
            playlistVbox.getChildren().add(playlist);
            PlaylistItemController controller = loader.getController();
            controller.setMyTunesController(this);

            // Create a new playlist and add it to the Library singleton
            Playlist playList = Library.getInstance().newPlaylist();
            controller.setPlaylist(playList);

            // Associate the playlist with its controller
            playList.setPlayListController(controller);

            // Store the controller in the UI node
            playlist.setUserData(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPlaylistTitle(Playlist playlist) {
        // Update playlist title in the sidebar
        for (Node node : playlistVbox.getChildren()) {
            if (node instanceof HBox) {
                PlaylistItemController itemController = (PlaylistItemController) node.getUserData();
                if (itemController != null && itemController.getPlaylist().getUuid().equals(playlist.getUuid())) {
                    itemController.updatePlaylistTitleUI();
                    break;
                }
            }
        }
    }

    public void onPlaylistSelected(Playlist playlist) {
        if (playlist != null) {
            switchToPlaylist(playlist);
        }
    }

    private void switchToPlaylist(Playlist playlist) {
        try {
            // Load the FXML for the playlist view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playlist-view.fxml"));
            BorderPane newView = loader.load();

            // Get the PlaylistViewController
            PlaylistViewController controller = loader.getController();

            // Set the playlist and other properties
            controller.setPlaylist(playlist);
            controller.setMyTunesController(this);
            controller.initializeCustom(); // Initialize the playlist data/UI

            // Replace the center view with the playlist view
            centerView.getChildren().clear();
            centerView.getChildren().add(newView);

            // Update the selected playlist reference
            selectedPlaylist = playlist;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void importSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.FLAC", "*.MP3", "*.WAV"));

        File selectedFile = fileChooser.showOpenDialog(centerView.getScene().getWindow());
        if (selectedFile != null) {
            SongFinder songFinder = new SongFinder();
            Song newSong = songFinder.findSong(selectedFile);

            // Work with the singleton instance of Library
            Library library = Library.getInstance();
            library.addSong(newSong);

            DataMethod.saveLibrary(library);

            logger.info("Added song: " + library.getSongs().getLast());

            if (!library.doesAlbumExist(newSong.getAlbumTitle())) {
                library.createNewAlbum(newSong);
            } else {
                Album album = library.findAlbum(newSong.getAlbumTitle());
                album.addSongToAlbum(newSong);
            }
        }
    }

    @FXML
    private void importSongsFromDirectory() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select Directory Containing Audio Files");

        File selectedDirectory = dirChooser.showDialog(centerView.getScene().getWindow());
        if (selectedDirectory != null && selectedDirectory.isDirectory()) {
            File[] audioFiles = selectedDirectory.listFiles(file ->
                    file.isFile() && (
                            file.getName().toLowerCase().endsWith(".flac") ||
                                    file.getName().toLowerCase().endsWith(".mp3") ||
                                    file.getName().toLowerCase().endsWith(".wav")
                    )
            );

            if (audioFiles != null && audioFiles.length > 0) {
                SongFinder songFinder = new SongFinder();

                for (File audioFile : audioFiles) {
                    Song newSong = songFinder.findSong(audioFile);

                    // Use the singleton instance of Library
                    Library library = Library.getInstance();
                    library.addSong(newSong);

                    DataMethod.saveLibrary(library);
                    logger.info("Saved updated library with new songs.");

                    logger.info("Added song: " + library.getSongs().getLast());

                    if (!library.doesAlbumExist(newSong.getAlbumTitle())) {
                        library.createNewAlbum(newSong);
                    } else {
                        Album album = library.findAlbum(newSong.getAlbumTitle());
                        album.addSongToAlbum(newSong);
                    }
                }
            } else {
                logger.info("No audio files found in the selected directory.");
            }
        } else {
            logger.info("No directory was selected or it is not valid.");
        }
    }


    public void updateSongUI(Song song) {
        if (song == null) {
            songTitleLabel.setText("No song playing");
            songArtistLabel.setText("");
            currentTimeLabel.setText("0:00 / 0:00");
        } else {
            songTitleLabel.setText(song.getSongTitle());
            songArtistLabel.setText(song.getSongArtist());

            String totalDuration = song.getSongDurationFormatted();

            // Bind current time to label
            mediaPlayer.getCurrentTimeProperty().addListener((observable, oldValue, newValue) -> {
                currentTimeLabel.setText(newValue + " / " + totalDuration);
            });

            currentTimeLabel.setText(mediaPlayer.getCurrentTimeProperty().get() + " / " + totalDuration);
        }
    }

    @FXML
    private void toggleSong()
    {
        if (mediaPlayer.getMediaPlayer() != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pauseSong();
            } else {
                mediaPlayer.resumeSong();
            }
        } else {
            Song firstSong = Library.getInstance().getSongs().getFirst();
            Playlist currentPlaylist = selectedPlaylist;
            if (firstSong != null) {
                mediaPlayer.getReadyToPlaySongInPlaylist(firstSong, currentPlaylist);
            } else {
                logger.warning("No songs are available to play.");
            }
        }
    }

    @FXML
    private void playNextSong()
    {
        if (mediaPlayer.getMediaPlayer() != null && mediaPlayer.isNextSongAvailable()) {
            Song nextSong = mediaPlayer.getNextSong();
            Playlist currentPlaylist = selectedPlaylist;
            if (nextSong != null && currentPlaylist != null) {
                mediaPlayer.getReadyToPlaySongInPlaylist(nextSong, currentPlaylist);
            } else {
                logger.warning("Next song or playlist is unavailable.");
            }
        } else {
            logger.warning("No more songs available in the playlist or no song is currently playing.");
        }
    }

    @FXML
    private void playPreviousSong()
    {
        if (mediaPlayer.getMediaPlayer() != null) {
            if (mediaPlayer.isPreviousSongAvailable()) {
                Song previousSong = mediaPlayer.getPreviousSong();
                Playlist currentPlaylist = selectedPlaylist;
                if (previousSong != null && currentPlaylist != null) {
                    mediaPlayer.getReadyToPlaySongInPlaylist(previousSong, currentPlaylist);
                } else {
                    logger.warning("Previous song or playlist is unavailable.");
                }
            } else {
                logger.warning("No previous song available.");
            }
        } else {
            logger.warning("Media player is not initialized.");
        }
    }

    public void playSongAtIndex(int index)
    {
        if (selectedPlaylist != null && index >= 0 && index < selectedPlaylist.getSongs().size()) {
            Song songToPlay = selectedPlaylist.getSongs().get(index);
            mediaPlayer.getReadyToPlaySongInPlaylist(songToPlay, selectedPlaylist);
        } else {
            logger.warning("Invalid song index or playlist is not selected.");
        }
    }

    @FXML
    public void initialize() {
        logger.info("MyTunesController initialized.");

        // Get the singleton instance of the Library
        Library library = Library.getInstance();
        logger.info("Library instance in use: " + library);

        // Debugging information
        System.out.println("Library loaded successfully with " + library.getSongs().size() + " songs and " + library.getPlaylists().size() + " playlists.");

        try {
            // Automatic import logic for songs from the "Music" folder
            File musicDirectory = new File(System.getProperty("user.home") + "/Documents/Music");

            if (!musicDirectory.exists() && !musicDirectory.mkdirs()) {
                logger.warning("Failed to create 'Music' folder: " + musicDirectory.getAbsolutePath());
                return;
            }

            if (musicDirectory.isDirectory()) {
                // Load songs from the directory
                File[] audioFiles = musicDirectory.listFiles(file ->
                        file.isFile() && file.getName().toLowerCase().endsWith(".mp3")
                );

                if (audioFiles != null && audioFiles.length > 0) {
                    SongFinder songFinder = new SongFinder();
                    for (File audioFile : audioFiles) {
                        Song newSong = songFinder.findSong(audioFile);
                        library.addSong(newSong);

                        if (!library.doesAlbumExist(newSong.getAlbumTitle())) {
                            library.createNewAlbum(newSong);
                        } else {
                            library.findAlbum(newSong.getAlbumTitle()).addSongToAlbum(newSong);
                        }
                    }
                    logger.info("Automatic import: " + audioFiles.length + " songs were loaded from the 'Music' folder in Documents.");
                } else {
                    logger.info("No MP3 files found in: " + musicDirectory.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.severe("Error during automatic import: " + e.getMessage());
        }
    }

    @FXML
    private void deletePlaylist() {
        Library library = Library.getInstance(); // Use the singleton instance of Library

        // Ensure there are playlists to delete
        if (library.playlists.isEmpty()) {
            logger.info("No playlists available for deletion.");
            return;
        }

        // Create a dialog to select a playlist
        ChoiceDialog<Playlist> dialog = new ChoiceDialog<>(library.playlists.get(0), library.playlists);
        dialog.setTitle("Delete Playlist");
        dialog.setHeaderText("Select a playlist to delete");
        dialog.setContentText("Choose a playlist:");

        // Show the dialog and capture the user selection
        dialog.showAndWait().ifPresent(selectedPlaylist -> {
            // Confirm deletion
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Are you sure you want to delete the playlist?");
            confirmationAlert.setContentText("Playlist: " + selectedPlaylist.getTitle());

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Delete the playlist from the library
                    library.playlists.remove(selectedPlaylist);

                    // Remove the playlist from the UI
                    playlistVbox.getChildren().removeIf(node -> {
                        if (node instanceof HBox) {
                            PlaylistItemController itemController = (PlaylistItemController) node.getUserData();
                            return itemController != null && itemController.getPlaylist().getUuid().equals(selectedPlaylist.getUuid());
                        }
                        return false;
                    });

                    if (this.selectedPlaylist != null && this.selectedPlaylist.getUuid().equals(selectedPlaylist.getUuid())) {
                        this.selectedPlaylist = null;

                        // Attempt to switch to another available playlist, if any exist
                        if (!library.playlists.isEmpty()) {
                            Playlist newPlaylist = library.playlists.get(0); // Select the first available playlist
                            switchToPlaylist(newPlaylist);
                        } else {
                            centerView.getChildren().clear(); // Clear the view if no playlists are left
                        }
                    }

                    // Log the deletion
                    logger.info("Deleted playlist: " + selectedPlaylist.getTitle());

                    // Reset selected playlist if it was the deleted one
                    if (this.selectedPlaylist != null && this.selectedPlaylist.getUuid().equals(selectedPlaylist.getUuid())) {
                        this.selectedPlaylist = null;
                    }
                }
            });
        });
    }

}
