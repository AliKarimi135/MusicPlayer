package ir.aliprogramer.musicplayer.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="musics" )
public class MusicModel {
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
    public MusicModel() {

    }

    public MusicModel(String title, String name, String path, String album, String albumId, String artist, String image) {
        Title = title;
        Name = name;
        Path = path;
        Album = album;
        AlbumId = albumId;
        Artist = artist;
        Image = image;
    }

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
}
