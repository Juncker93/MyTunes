package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.Serializable;

public class Song implements Serializable
{
    private static final long serialVersionUID = 1L;

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
    private transient File songFile;

    private String songFilePath;

    public Song(String title, String artist, String album, String songYear, int duration, byte[] albumCover)
    {
        this.songTitle = title;
        this.songArtist = artist;
        this.songYear = songYear;
        this.songDuration = duration;
        this.albumTitle = album;
        this.albumCoverByte = albumCover;
        setAlbumCoverBytes(albumCover);

        // Debug log
        System.out.println("Song instance created: " + this + " with title: " + this.songTitle);
    }

    public Image getAlbumCover()
    {
        return new Image(new ByteArrayInputStream(albumCoverByte));
    }

    public void setAlbumCoverBytes(byte[] albumCoverBytes) {
        this.albumCoverByte = albumCoverBytes;
        if (albumCoverBytes != null) {
            this.albumCover = new Image(new ByteArrayInputStream(albumCoverBytes));
        }
    }

    public String getSongDurationFormatted()
    {
        int minutes = songDuration / 60; // Get full minutes
        int seconds = songDuration % 60; // Get remaining seconds

        // Format seconds to always be 2 digits
        return String.format("%d:%02d", minutes, seconds);
    }

    public String getSongFilePath() {
        return songFile != null ? songFile.getAbsolutePath() : songFilePath;
    }

    public void setSongFile(File file) {
        this.songFile = file;
        this.songFilePath = file.getAbsolutePath();
    }

    public void restoreSongFile() {
        if (songFilePath != null) {
            songFile = new File(songFilePath);
        }
    }


    @Override
    public String toString()
    {
        return songTitle + " - " + songArtist + " - " + albumTitle + " - ";
    }
}
