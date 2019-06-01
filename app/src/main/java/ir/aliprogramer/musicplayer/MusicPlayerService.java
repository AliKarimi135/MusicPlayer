package ir.aliprogramer.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;

import ir.aliprogramer.musicplayer.adapter.DescriptionAdapter;

public class MusicPlayerService extends Service {

    ExoPlayer exoPlayer;
    String path="";
    PlayerNotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this,new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null)
            return START_STICKY;
        path=intent.getStringExtra("path");

        Log.d("service",path);
        DataSpec dataSpec = new DataSpec(Uri.parse(path));
        final FileDataSource fileDataSource = new FileDataSource();
        try {
            fileDataSource.open(dataSpec);
        } catch (FileDataSource.FileDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return fileDataSource;
            }
        };
        MediaSource audioSource = new ExtractorMediaSource(fileDataSource.getUri(),
                factory, new DefaultExtractorsFactory(), null, null);
        exoPlayer.prepare(audioSource);
       MainActivity activity=MainActivity.instance;
       if(activity!=null)
           activity.setPlayer(exoPlayer);
       setNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void setNotification() {
        notificationManager= new PlayerNotificationManager(
                this,
                "موزیک",
                1,
                new DescriptionAdapter(path));
        notificationManager.setFastForwardIncrementMs(0);
        notificationManager.setRewindIncrementMs(0);
        notificationManager.setColor(Color.BLUE);
        notificationManager.setPlayer(exoPlayer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
        notificationManager.setPlayer(null);
    }
    public ExoPlayer getCurrentExoplayer(String path){
        this.path=path;
        Log.d("serv",path);
        return this.exoPlayer;
    }
}
