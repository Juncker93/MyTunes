package com.example.mytunes;

import javafx.fxml.FXML;

import lombok.Setter;

public class PlaylistItemController
{
    @Setter
    MyTunesController myTunesController;
    @Setter
    Library library;
    @Setter
    Playlist playlist;

    public void PlaylistItemController()
    {

    }

    @FXML
    private void onPressedPlaylist() {
        if (myTunesController != null) {
            myTunesController.onPlaylistSelected(playlist);
        }
    }
}
