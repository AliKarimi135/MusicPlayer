package ir.aliprogramer.musicplayer.adapter;

import android.app.PendingIntent;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;


public class DescriptionAdapter implements PlayerNotificationManager.MediaDescriptionAdapter {

String musicPath;
String name;
int end;
    public DescriptionAdapter(String musicPath) {
        this.musicPath = musicPath;
        end=musicPath.lastIndexOf('/');
       name=musicPath;
       name=name.substring(end+1,musicPath.length());
    }

    @Override
    public String getCurrentContentTitle(Player player) {
        int window = player.getCurrentWindowIndex();
        //return getTitle(window);
        return name;
    }

    @Nullable
    @Override
    public String getCurrentContentText(Player player) {
        int window = player.getCurrentWindowIndex();
        // return getDescription(window);
        return null;
    }
    @Nullable
    @Override
    public Bitmap getCurrentLargeIcon(Player player,
                                      PlayerNotificationManager.BitmapCallback callback) {
        int window = player.getCurrentWindowIndex();
        /*Bitmap largeIcon = getLargeIcon(window);
        if (largeIcon == null && getLargeIconUri(window) != null) {
            // load bitmap async
            loadBitmap(getLargeIconUri(window), callback);
            return getPlaceholderBitmap();
        }
        return largeIcon;*/
        return null;
    }

    @Nullable
    @Override
    public PendingIntent createCurrentContentIntent(Player player) {
        int window = player.getCurrentWindowIndex();
        //return createPendingIntent(window);
        return null;
    }
}
