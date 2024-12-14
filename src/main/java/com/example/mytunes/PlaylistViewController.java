package com.example.mytunes;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.SearchableComboBox;

import java.awt.*;

public class PlaylistViewController {
    @Setter
    MyTunesController myTunesController;

    @FXML
    private TextField changePlaylistName;

    @FXML
    private TableView<Song> tableviewPlaylist;

    @FXML
    private TableColumn<Song, Image> songCoverColumn;

    @FXML
    private TableColumn<Song, Integer> songNumberColumn;

    @FXML
    private TableColumn<Song, Integer> songTimeColumn;

    @FXML
    private TableColumn<Song, String> songTitleColumn;

    @FXML
    private TableColumn<Song, String> albumColumn;

    @FXML
    private TableColumn<Song, String> artistColumn;

    @FXML
    private SearchableComboBox<Song> searchableComboBox;

    @Setter
    private Playlist playlist;

    @Setter
    private Library library;

    private void setChangePlaylistName()
    {
        changePlaylistName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (playlist != null) {
                playlist.setTitle(newValue);
                myTunesController.setPlaylistTitle(playlist);
            }
        });
    }

    public void initializeCustom()
    {
        setupCells();
        setChangePlaylistName();
        loadPlaylistData(); // Load the initial playlist data
        addSongsToSearchBox();
    }

    @FXML
    private void addSong()
    {
        playlist.getSongs().add(searchableComboBox.getSelectionModel().getSelectedItem());
        refreshTableview(); // Update the table view after adding a song
    }

    @FXML
    private void removeSong() {
        // Get the selected song from the TableView
        Song selectedSong = tableviewPlaylist.getSelectionModel().getSelectedItem();

        if (selectedSong != null && playlist != null) {
            // Check if the selected song is currently playing
            if (myTunesController.getMediaPlayer().getCurrentSong() == selectedSong) {
                // Stop and dispose of the current playback, then remove the song
                myTunesController.getMediaPlayer().stopSong();
                myTunesController.getMediaPlayer().getMediaPlayer().dispose(); // Dispose of media player resources

                playlist.getSongs().remove(selectedSong);

                if (!playlist.getSongs().isEmpty()) {
                    // Play the next song in the playlist (or start from the first one)
                    myTunesController.playSongAtIndex(0); // Playing the first song in the updated playlist
                } else {
                    // Playlist is empty - stop playback and update UI
                    myTunesController.getMediaPlayer().stopSong();
                    myTunesController.updateSongUI(null); // Reset the UI to show no song is playing
                    System.out.println("Playlist is empty. Stopping playback.");
                }
            } else {
                // Just remove the song if it is not currently playing
                playlist.getSongs().remove(selectedSong);
            }

            // Update the UI to reflect the changes
            refreshTableview();
            System.out.println("Removed song: " + selectedSong.getSongTitle());
        } else {
            System.out.println("No song selected or playlist is null.");
        }
    }

    private void addSongsToSearchBox()
    {
        searchableComboBox.getItems().clear();
        searchableComboBox.getItems().addAll(library.getSongs());
    }

    private void refreshTableview()
    {
        tableviewPlaylist.getItems().clear();
        tableviewPlaylist.getItems().addAll(playlist.getSongs());
        tableviewPlaylist.refresh();
    }

    public void loadPlaylistData()
    {
        if (playlist != null) {
            changePlaylistName.setText(playlist.getTitle());
            refreshTableview(); // Ensure table view is updated with current playlist songs
        }
    }

    private void setupCells()
    {
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("albumTitle"));
        songTimeColumn.setCellValueFactory(new PropertyValueFactory<>("songDurationFormatted"));
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("songTitle"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("songArtist"));

        songNumberColumn.setCellValueFactory(param -> {
            int rowIndex = tableviewPlaylist.getItems().indexOf(param.getValue());
            return new javafx.beans.property.SimpleIntegerProperty(rowIndex + 1).asObject();
        });

        // Add a listener for song selection
        tableviewPlaylist.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedIndex = tableviewPlaylist.getSelectionModel().getSelectedIndex();
                myTunesController.playSongAtIndex(selectedIndex);
            }
        });
    }
}
