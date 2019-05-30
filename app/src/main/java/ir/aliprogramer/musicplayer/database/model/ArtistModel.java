package ir.aliprogramer.musicplayer.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "artist_tb")
public class ArtistModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int Id;
    @ColumnInfo(name = "artist")
    String Name;
    @ColumnInfo(name = "path")
    String Path;
    @ColumnInfo(name = "image")
    String Image;
    @ColumnInfo(name = "track")
    int NumberTrack;
    @ColumnInfo(name = "numberAlbum")
    int NumberAlbum;

    public ArtistModel() {
    }

    public ArtistModel(String name, String path, String image, int numberAlbum, int numberTrack) {
        Name = name;
        Path = path;
        Image = image;
        NumberAlbum = numberAlbum;
        NumberTrack = numberTrack;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public int getNumberAlbum() {
        return NumberAlbum;
    }

    public void setNumberAlbum(int numberAlbum) {
        NumberAlbum = numberAlbum;
    }

    public int getNumberTrack() {
        return NumberTrack;
    }

    public void setNumberTrack(int numberTrack) {
        NumberTrack = numberTrack;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
