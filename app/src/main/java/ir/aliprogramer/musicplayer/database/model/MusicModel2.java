package ir.aliprogramer.musicplayer.database.model;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public class MusicModel2 {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int Id;
    @ColumnInfo(name = "title")
    String Title;
    @ColumnInfo(name = "name")
    String Name;
    @ColumnInfo(name = "path")
    String Path;
    @ColumnInfo(name = "album")
    String Album;
    @ColumnInfo(name = "albumId")
    String AlbumId;
    @ColumnInfo(name = "artist")
    String Artist;
    @ColumnInfo(name = "image")
    String Image;
    @ColumnInfo(name = "position")
    int Position;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }
}
