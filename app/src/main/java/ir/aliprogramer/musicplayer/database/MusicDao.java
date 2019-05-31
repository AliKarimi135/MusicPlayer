package ir.aliprogramer.musicplayer.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ir.aliprogramer.musicplayer.database.model.ArtistModel;
import ir.aliprogramer.musicplayer.database.model.FolderModel;
import ir.aliprogramer.musicplayer.database.model.MusicModel;

@Dao
public interface  MusicDao {
    @Query("SELECT * FROM musics")
    List<MusicModel> getAllMusic();

    @Insert
    void insertMusic(MusicModel music);

    @Insert
    void insertFolder(FolderModel folderModel);

    @Query("SELECT * FROM folders ORDER BY name")
    List<FolderModel> getAllFolder();

    @Query("SELECT name FROM folders WHERE  path=:fpath")
    String checkFolderName(String fpath);


   // @Query("SELECT a.* FROM musics a JOIN (SELECT album FROM musics GROUP BY albumId HAVING COUNT(albumId)>=1) b ON a.album=b.album ORDER BY artist ")
   @Query("SELECT *  FROM musics GROUP BY albumId HAVING count(DISTINCT(albumId))>=1")
    List<MusicModel>getAllAlbum();

    @Query("SELECT id,artist,path,image,count(name) AS track,count(DISTINCT albumId) AS numberAlbum FROM musics GROUP BY artist ORDER BY artist")
    List<ArtistModel> getAllArtist();

    @Query("SELECT *  FROM musics   WHERE path=:fpath")
    List<MusicModel>getMusicListForFolder(String fpath);

    @Query("SELECT *  FROM musics WHERE path=:fpath AND albumId=:albumid")
    List<MusicModel>getMusicListForAlbum(String fpath,String albumid);

    @Query("SELECT *  FROM musics WHERE artist=:artist")
    List<MusicModel>getMusicListForArtist(String artist);

}
