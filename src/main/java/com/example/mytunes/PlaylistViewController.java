package com.example.mytunes;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.SearchableComboBox;

public class PlaylistViewController
{
    @Setter
    MyTunesController myTunesController;

    @FXML
    private TextField changePlaylistName;

    @FXML
    private TableView<Song> tableviewPlaylist;

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
        tableviewPlaylist.getItems().addAll(playlist.getSongs());
    }

}
