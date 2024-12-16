package com.example.mytunes;

import javafx.scene.image.Image;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;

public class Library implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static Library instance;

    @Getter
    ArrayList<Playlist> playlists = new ArrayList<>();

    @Getter
    ArrayList<Song> songs = new ArrayList<>();

    @Getter
    ArrayList<Album> albums = new ArrayList<>();

    public static void initializeLibrary() {
        // Load the library data
        Library loadedLibrary = DataMethod.loadLibrary();

        // Check if loadedLibrary is not null and update the singleton instance
        if (loadedLibrary != null) {
            Library.setInstance(loadedLibrary);
        } else {
            // If no saved library exists, create a new instance
            Library.setInstance(new Library());
        }
    }

    public static synchronized Library getInstance() {
        if (instance == null) {
            synchronized (Library.class) {
                if (instance == null) {  // Double-checked locking for thread safety
                    instance = DataMethod.loadLibrary();
                    if (instance == null) {
                        instance = new Library();
                    }
                }
            }
        }
        return instance;
    }

    public static synchronized void setInstance(Library newInstance) {
        instance = newInstance;
    }


    public Library() {
        System.out.println("Library instance created: " + this);
    }

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
