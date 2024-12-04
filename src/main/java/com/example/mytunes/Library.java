package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;

import java.util.ArrayList;

public class Library
{
    ArrayList<Playlist> playlists = new ArrayList<>();

    @Getter
    ArrayList<Song> songs = new ArrayList<>();
    ArrayList<Album> albums = new ArrayList<>();

    public void addSong(Song song)
    {
        songs.add(song);
    }

    public void createNewAlbum(Song song)
    {
        String albumTitle = song.getAlbumTitle();
        String albumArtist = song.getSongArtist();
        String albumYear = song.getSongYear();
        Image albumCover = song.getAlbumCover();

        Album newAlbum = new Album(albumTitle, albumArtist, albumYear, albumCover);

        addAlbum(newAlbum);
    }

    public void addAlbum(Album album){ albums.add(album); }

    public boolean doesAlbumExist(String albumTitle)
    {
        for (Album album : albums){
            if
                (album.getAlbumTitle().equals(albumTitle)) return true;
        }
        return false;
    }

    public Album findAlbum(String albumTitle)
    {
        for (Album album : albums){
            if (album.getAlbumTitle().equals(albumTitle))
                return album;
        }
        return null;
    }

    public Playlist newPlaylist()
    {
        Playlist playlist = new Playlist("New playlist", 0);
        playlists.add(playlist);
        return playlist;
    }
}
