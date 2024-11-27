package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;

import java.util.ArrayList;


public class Album
{
    @Getter
    private String albumTitle;
    private String albumArtist;
    private String albumGenre;
    private String albumDate;

    @Getter
    private Image albumCover;

    private ArrayList<Song> songs = new ArrayList<>();

    public Album(String title, String artist, String date, Image albumCover)
    {
        this.albumTitle = title;
        this.albumArtist = artist;
        //this.albumGenre = genre;
        this.albumDate = date;
        this.albumCover = albumCover;
    }

    public void addSongToAlbum(Song song)
    {
        this.songs.add(song);
    }
}
