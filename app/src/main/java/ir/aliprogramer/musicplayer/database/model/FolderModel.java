package ir.aliprogramer.musicplayer.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "folders")
public class FolderModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int id;
    @ColumnInfo(name = "name")
    String Name;
    @ColumnInfo(name = "path")
    String Path;
    @ColumnInfo(name = "album")
    String Album;
    @ColumnInfo(name = "artist")
    String Artist;

    public FolderModel() {
    }

    public FolderModel(String name, String path, String album, String artist) {
        Name = name;
        Path = path;
        Album = album;
        Artist = artist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }
}
