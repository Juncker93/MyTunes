package com.example.mytunes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class DataMethod {
    private static final String SAVE_DIRECTORY = System.getProperty("user.home") + "/Documents/MyTunes";

    // Ensure the save directory exists
    private static void ensureSaveDirectory() throws IOException {
        Path path = Paths.get(SAVE_DIRECTORY);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    // Save a Library object to a file
    public static void saveLibrary(Library library) {
        // Kontrollér, om det er singleton-instansen
        if (library != Library.getInstance()) {
            System.err.println("Warning: Attempting to save a different library instance!");
            return;
        }

        // Kontrollér, om biblioteket er tomt
        if (library.getSongs().isEmpty() && library.getPlaylists().isEmpty()) {
            System.err.println("Library is empty. Saving has been skipped.");
            return;
        }

        try {
            File directory = new File(SAVE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs(); // Create directory if it doesn't exist
            }
            File file = new File(directory, "library.dat");

            // Gem biblioteket i filen
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(library);
                System.out.println("Library saved successfully with "
                        + library.getSongs().size() + " songs and "
                        + library.getPlaylists().size() + " playlists.");
            }
        } catch (IOException e) {
            System.err.println("Error saving library: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Library loadLibrary() {
        Library library = null;
        File file = new File(SAVE_DIRECTORY + "/library.dat");

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                library = (Library) ois.readObject();
                System.out.println("Library loaded successfully with "
                        + library.getSongs().size() + " songs and "
                        + library.getPlaylists().size() + " playlists.");

                // Gendan transient felter som filer og billeder
                for (Song song : library.getSongs()) {
                    song.restoreSongFile();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading library: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Library file not found, creating a new one.");
        }

        if (library == null) {
            library = new Library();
        }

        // Opdater singleton-instansen
        Library.setInstance(library);
        return Library.getInstance();
    }

    // Save an individual Playlist object
    public static void savePlaylist(Playlist playlist) {
        try {
            ensureSaveDirectory();
            File file = new File(SAVE_DIRECTORY, "playlist_" + playlist.getUuid() + ".dat");

            // Gem spillelisten i filen
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(playlist);
            }
            System.out.println("Playlist saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving the playlist: " + e.getMessage());
        }
    }

    // Load an individual Playlist object
    public static Playlist loadPlaylist(UUID uuid) {
        Playlist playlist = null;
        File file = new File(SAVE_DIRECTORY, "playlist_" + uuid + ".dat");

        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                // Indlæs spillelisten fra filen
                playlist = (Playlist) ois.readObject();

                // Gendan transient felter for spillelisten
                for (Song song : playlist.getSongs()) {
                    song.restoreSongFile();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.err.println("Error loading the playlist: " + e.getMessage());
            }
        } else {
            System.out.println("No saved playlist found with UUID: " + uuid);
        }

        return playlist;
    }
}