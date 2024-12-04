package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.File;

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
    public transient Image albumCover;

    @Getter @Setter
    private byte[] albumCoverByte;

    @Getter @Setter
    private File songFile;

    public Song(String title, String artist, String album, String songYear, int duration, byte[] albumCover)
    {
        this.songTitle = title;
        this.songArtist = artist;
        this.songYear = songYear;
        this.songDuration = duration;
        this.albumTitle = album;
        this.albumCoverByte = albumCover;
        setAlbumCoverBytes(albumCover);
    }

    public Image getAlbumCover()
    {
        return new Image(new ByteArrayInputStream(albumCoverByte));
    }

    public void setAlbumCoverBytes(byte[] albumCoverBytes)
    {
        albumCover = new Image(new ByteArrayInputStream(albumCoverBytes));
    }

    public String getSongDurationFormatted()
    {
        int minutes = songDuration % 60;
        int seconds = songDuration / 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public String toString()
    {
        return songTitle + " - " + songArtist + " - " + albumTitle + " - ";
    }
}
