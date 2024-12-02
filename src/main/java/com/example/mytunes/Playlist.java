package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

public class Playlist
{
    @Setter @Getter
    private String title;
    @Setter
    private PlaylistItemController playListController;
    private int duration;
    private Image imagePlaylist;

    public Playlist(String title, int duration)
    {
        this.title = title;
        this.duration = duration;
    }

    public Playlist()
    {

    }

    public void setTitle(String title)
    {
        this.title = title;
        playListController.setPlaylistTitle(title);
    }
}
