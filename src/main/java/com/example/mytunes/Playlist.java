package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.UUID;

public class Playlist
{
    @Setter @Getter
    public UUID Uuid = UUID.randomUUID();
    @Setter @Getter
    private String title;
    @Setter
    private PlaylistItemController playListController;
    private int duration;
    private Image imagePlaylist;

    @Setter @Getter
    ArrayList<Song> songs = new ArrayList<>();

    public Playlist(String title, int duration)
    {
        this.title = title;
        this.duration = duration;
    }

    public Playlist()
    {

    }

    // Ensures that the playlist title is shown in the ChoiceDialog
    @Override
    public String toString() {
        return title + " (" + songs.size() + " songs)";
    }


}
