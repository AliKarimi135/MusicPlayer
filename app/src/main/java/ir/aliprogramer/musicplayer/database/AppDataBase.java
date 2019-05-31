package ir.aliprogramer.musicplayer.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import ir.aliprogramer.musicplayer.database.model.ArtistModel;
import ir.aliprogramer.musicplayer.database.model.FolderModel;
import ir.aliprogramer.musicplayer.database.model.MusicModel;
import ir.aliprogramer.musicplayer.database.model.PlayListModel;

@Database(entities = {MusicModel.class, FolderModel.class, ArtistModel.class, PlayListModel.class},version = 1)
public  abstract class AppDataBase extends RoomDatabase {
    public abstract MusicDao dao();
    private static AppDataBase instance;

    public static AppDataBase getInstance(Context context){
        if(instance==null)
            instance= Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"music_database")
                    .allowMainThreadQueries().build();

        return instance;
    }
}
