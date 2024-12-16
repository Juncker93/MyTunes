package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Playlist implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Setter @Getter
    private UUID Uuid = UUID.randomUUID();
    @Setter @Getter
    private String title;
    @Setter
    private transient PlaylistItemController playListController;
    private int duration;

    @Getter
    private transient Image imagePlaylist;

    @Setter @Getter
    ArrayList<Song> songs = new ArrayList<>();

    public Playlist(String title, int duration)
    {
        this.title = title;
        this.duration = duration;

        System.out.println("Playlist instance created: " + this + " with title: " + this.title);
    }

    public Playlist() {
        System.out.println("Empty Playlist instance created: " + this);
    }

    // Ensures that the playlist title is shown in the ChoiceDialog
    @Override
    public String toString() {
        return title + " (" + songs.size() + " songs)";
    }


}
