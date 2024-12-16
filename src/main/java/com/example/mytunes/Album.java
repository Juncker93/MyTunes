package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class Album implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Getter
    private String albumTitle;
    private String albumArtist;
    private String albumGenre;
    private String albumDate;

    private byte[] albumCoverByte;

    @Getter
    private transient Image albumCover;

    @Getter
    private ArrayList<Song> songs = new ArrayList<>();

    public Album(String title, String artist, String date, Image albumCover)
    {
        this.albumTitle = title;
        this.albumArtist = artist;
        //this.albumGenre = genre;
        this.albumDate = date;
        setAlbumCover(albumCover);

        // Debug log
        System.out.println("Album instance created: " + this + " with title: " + this.albumTitle);
    }

    public void addSongToAlbum(Song song)
    {
        this.songs.add(song);
    }

    public void setAlbumCover(Image albumCover) {
        this.albumCover = albumCover;
        if (albumCover != null) {
            // Convert Image to byte array (implement helper function for this)
            this.albumCoverByte = convertImageToBytes(albumCover);
        }
    }

    public void restoreAlbumCover() {
        if (albumCoverByte != null) {
            this.albumCover = new Image(new ByteArrayInputStream(albumCoverByte));
        }
    }

    private byte[] convertImageToBytes(Image image) {
        // Implement this method to convert an Image to byte[]
        return null; // Placeholder
    }

}
