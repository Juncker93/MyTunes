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

public class PlaylistViewController
{
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
        changePlaylistName.textProperty().addListener((observable, oldValue, newValue) ->{
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
        changePlaylistName.setText(playlist.getTitle());
        addSongsToSearchBox();
    }

    @FXML
    private void addSong()
    {
        playlist.getSongs().add(searchableComboBox.getSelectionModel().getSelectedItem());
        addSongsToTableview();
    }

    private void addSongsToSearchBox()
    {
        searchableComboBox.getItems().clear();
        searchableComboBox.getItems().addAll(library.getSongs());
    }

    private void addSongsToTableview()
    {
        tableviewPlaylist.getItems().clear();

        for(Song song : playlist.getSongs()){
            tableviewPlaylist.getItems().add(song);
        }

        tableviewPlaylist.refresh();
    }

    private void setupCells() {
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("albumTitle"));

        // Set up other columns as needed
        songTimeColumn.setCellValueFactory(new PropertyValueFactory<>("songDurationFormatted"));
        songTitleColumn.setCellValueFactory(new PropertyValueFactory<>("songTitle"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("songArtist"));


        songNumberColumn.setCellValueFactory(param -> {
            int rowIndex = tableviewPlaylist.getItems().indexOf(param.getValue());
            return new javafx.beans.property.SimpleIntegerProperty(rowIndex + 1).asObject();
        });
    }

}
