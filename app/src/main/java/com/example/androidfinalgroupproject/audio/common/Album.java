package com.example.androidfinalgroupproject.audio.common;

/**
 * @Author: Yanan Cheng
 * @Date: 2020/11/19 5:42
 * @Version: 1.0
 */
public class Album {
    private String idAlbum;
    private String artist;
    private String strAlbum;

    /**
     * it includes id, song name, and artist who sings the song
     */

    public Album() {
    }

    public Album(String idAlbum, String artist, String strAlbum) {
        this.idAlbum = idAlbum;
        this.artist = artist;
        this.strAlbum = strAlbum;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getStrAlbum() {
        return strAlbum;
    }

    public void setStrAlbum(String strAlbum) {
        this.strAlbum = strAlbum;
    }
}
