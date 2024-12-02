package com.example.mytunes;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Setter;

public class PlaylistViewController
{

    MyTunesController myTunesController;
    @FXML
    private TextField changePlaylistName;
    @Setter
    private Playlist playlist;
    @Setter
    private Library library;

    private void setChangePlaylistName()
    {
        changePlaylistName.textProperty().addListener((observable, oldValue, newValue) ->{
            if (playlist != null) {
                playlist.setTitle(newValue);
                myTunesController.setPlaylistTitle(playlist.getTitle());
            }
        });
    }

    public void initializeCustom()
    {
        setChangePlaylistName();
        changePlaylistName.setText(playlist.getTitle());
    }

}
