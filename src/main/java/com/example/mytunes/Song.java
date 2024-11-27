package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

public class Song
{
    @Getter @Setter
    private String songTitle;

    @Getter @Setter
    private String songArtist;

    @Getter @Setter
    private String songYear;

    @Getter @Setter
    private int songDuration;

    @Getter @Setter
    private String albumTitle;

    @Getter @Setter
    private Image albumCover;

    public Song(String title, String artist, String album, String songYear, int duration, Image albumCover)
    {
        this.songTitle = title;
        this.songArtist = artist;
        this.songYear = songYear;
        this.songDuration = duration;
        this.albumTitle = album;
        this.albumCover = albumCover;
    }

    @Override
    public String toString()
    {
        return songTitle + " - " + songArtist + " - " + albumTitle + " - ";
    }
}
