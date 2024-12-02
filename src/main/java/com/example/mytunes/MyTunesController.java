package com.example.mytunes;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MyTunesController
{
    @FXML
    private VBox playlistVbox;
    @FXML
    private AnchorPane centerView;

    Library library = new Library();

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
        } catch (IOException e) {
        e.printStackTrace();
        }
    }

    public void setPlaylistTitle(String title)
    {
        setPlaylistTitle.setText(title);
    }

    public void onPlaylistSelected(Playlist playlist)
    {
        if (playlist != null){
            switchToPlaylist(playlist);
        }
    }

    private void switchToPlaylist(Playlist playlist) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playlist-view.fxml"));
            BorderPane newView = loader.load();

            PlaylistViewController controller = loader.getController(); // Get PlaylistViewController
            controller.setPlaylist(playlist); // Set the selected playlist in the PlaylistViewController
            controller.setLibrary(library);// Optionally, pass the user library to the playlist view
            controller.myTunesController(this);
            controller.initializeCustom();

            selectedPlaylist = playlist;

            centerView.getChildren().add(newView); // Set the new view in the center of the BorderPane
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupUserDocuments()
    {
        String
    }
}