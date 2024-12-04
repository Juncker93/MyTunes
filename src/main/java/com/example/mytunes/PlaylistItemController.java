package com.example.mytunes;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

public class PlaylistItemController
{
    @Setter
    MyTunesController myTunesController;
    @Setter
    Library library;
    @Setter @Getter
    Playlist playlist;
    @FXML
    private Label setPlaylistTitle;

    public void PlaylistItemController()
    {

    }

    @FXML
    private void onPressedPlaylist() {
        if (myTunesController != null) {
            myTunesController.onPlaylistSelected(playlist);
        }
    }

    public void updatePlaylistTitleUI()
    {
        setPlaylistTitle.setText(playlist.getTitle());
    }
}
