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
import javafx.stage.FileChooser;

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

    private MediaPlayer mediaPlayer = new MediaPlayer(this);
    private Library library = new Library();
    private Logger logger = Logger.getLogger(MyTunesController.class.getName());
    private Playlist selectedPlaylist;

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

    public void onPlaylistSelected(Playlist playlist)
    {
        if (playlist != null) {
            switchToPlaylist(playlist);
        }
    }

    private void switchToPlaylist(Playlist playlist)
    {
        try {
            // Load playlist view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playlist-view.fxml"));
            BorderPane newView = loader.load();

            // Get PlaylistViewController
            PlaylistViewController controller = loader.getController();
            controller.setPlaylist(playlist); // Set the selected playlist in the PlaylistViewController
            controller.setLibrary(library); // Optionally, pass the user library to the playlist view
            controller.setMyTunesController(this);
            controller.initializeCustom(); // Initialize the PlaylistViewController

            // Replace the center view
            centerView.getChildren().clear(); // Clear any existing views
            centerView.getChildren().add(newView); // Add the new view

            selectedPlaylist = playlist; // Update selectedPlaylist reference
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void importSong()
    {
        FileChooser fil_chooser = new FileChooser();
        fil_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio Files", "*.FLAC", "*.MP3", "*.WAV"));

        File selectedFile = fil_chooser.showOpenDialog(centerView.getScene().getWindow());
        if (selectedFile != null) {
            SongFinder songFinder = new SongFinder();
            Song newSong = songFinder.findSong(selectedFile);
            library.addSong(newSong);

            logger.info("Added song: " + library.getSongs().getLast());

            if (!library.doesAlbumExist(newSong.getAlbumTitle())) {
                library.createNewAlbum(newSong);
            } else {
                Album album = library.findAlbum(newSong.getAlbumTitle());
                album.addSongToAlbum(newSong);
            }
        }
    }

    public void updateSongUI(Song song)
    {
        if (song == null) {
            songTitleLabel.setText("No song playing");
            songArtistLabel.setText("");
            currentTimeLabel.setText("0:00");
        } else {
            songTitleLabel.setText(song.getSongTitle());
            songArtistLabel.setText(song.getSongArtist());
            currentTimeLabel.setText("0:00");
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
            Song firstSong = library.getSongs().getFirst();
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

}
