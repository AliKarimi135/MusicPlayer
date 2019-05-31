package ir.aliprogramer.musicplayer;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;
import java.util.List;

import ir.aliprogramer.musicplayer.adapter.ViewPagerAdapter;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.FolderModel;
import ir.aliprogramer.musicplayer.database.model.MusicModel;
import ir.aliprogramer.musicplayer.fragment.AlbumFragment;
import ir.aliprogramer.musicplayer.fragment.ArtistFragment;
import ir.aliprogramer.musicplayer.fragment.FolderFragment;
import ir.aliprogramer.musicplayer.fragment.MusicFragment;
import ir.aliprogramer.musicplayer.fragment.PlayListFragment;

public class MainActivity extends AppCompatActivity {
    AppPreferenceTools appPreferenceTools;
    Toolbar toolbar;
    TextView title;
    TabLayout tabLayout;
    ViewPager viewPager;
    List<MusicModel>musicList=new ArrayList<>();
    List<FolderModel>folderList=new ArrayList<>();
    ViewPagerAdapter viewPagerAdapter;
    MusicFragment musicFragment;
    FolderFragment folderFragment;
    AlbumFragment albumFragment;
    ArtistFragment artistFragment;
    PlayListFragment playListFragment;
    PlayerView playerView;
    ExoPlayer exoPlayer;
    FrameLayout frameLayout;
    LinearLayout container;
    String pathMusic;
    MusicPlayerService playerService=new MusicPlayerService();

    static MainActivity instance;
    FragmentManager manager;
    android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        title=toolbar.findViewById(R.id.title);
        title.setText(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        instance=this;

        frameLayout=findViewById(R.id.frame_layout);
        container=findViewById(R.id.container);


        tabLayout=findViewById(R.id.tab);
        viewPager=findViewById(R.id.viewpager);

        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        manager=getSupportFragmentManager();

        musicFragment=new MusicFragment();
        folderFragment=new FolderFragment(manager);
        albumFragment=new AlbumFragment(manager);
        artistFragment=new ArtistFragment(manager);
        playListFragment=new PlayListFragment();
        viewPagerAdapter.addFragment(folderFragment,getString(R.string.folder));
        viewPagerAdapter.addFragment(musicFragment,getString(R.string.music));
        viewPagerAdapter.addFragment(albumFragment,getString(R.string.album));
        viewPagerAdapter.addFragment(artistFragment,getString(R.string.artist));
        viewPagerAdapter.addFragment(playListFragment,getString(R.string.play_list));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        appPreferenceTools=new AppPreferenceTools(this);
        if(!appPreferenceTools.getFirstSetup()){
            appPreferenceTools.setFirstSetup(true);
            checkPermition();
        }
        if(savedInstanceState!=null)
            pathMusic=savedInstanceState.getString("pathService");

        playerView=findViewById(R.id.player);
    }

    @Override
    public void onBackPressed() {
        Log.d("numberStack",manager.getBackStackEntryCount()+"");
        if(manager.getBackStackEntryCount()>0){
            container.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            title.setText(getString(R.string.app_name));
        }else{
            exoPlayer.release();
            this.finish();
            super.onBackPressed();
        }

    }

    public void showFrameLayout(String title2){
        frameLayout.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
        title.setText(title2);
    }
    private boolean isServiceRunning(MusicPlayerService serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getClass().equals(service.service.getClass())) {
                playerService=serviceClass;
                return true;
            }
        }
        return false;
    }

    private void initializePlayer() {
       exoPlayer =ExoPlayerFactory.newSimpleInstance(this,new DefaultRenderersFactory(this),
                                                        new DefaultTrackSelector(), new DefaultLoadControl());

    }
    public void play(String path){
        playerView.setVisibility(View.VISIBLE);
        pathMusic=path;
        Intent serviceIntent = new Intent(this,playerService.getClass());
        serviceIntent.putExtra("path", pathMusic);

        this.startService(serviceIntent);

    }

    public void setPlayer(ExoPlayer exoPlayer1){
        exoPlayer=exoPlayer1;
        playerView.setPlayer(exoPlayer1);
    }


    public void checkPermition(){
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, PERMISSIONS, 100);
    }else {
        getMusicTask();
    }

}

    // Method to read all the audio/MP3 files.
    public void getAllAudioFromDevice() {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        MusicModel musicModel;
        FolderModel folderModel;
        String folderName,folderPath,path;
        int end,counter=0;
        ImageSaver imageSaver=new ImageSaver(getApplicationContext()).setDirectoryName("musicImageDir");
        imageSaver.setExternal(true);

        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE,
                                MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,
                                MediaStore.Audio.AlbumColumns.ALBUM_ID};
        Cursor c = this.getContentResolver().query(uri, projection, null,null, null);


        if (c != null) {
            while (c.moveToNext()) {
                // Log.d("album",c.getString(2)+"\talbum_id:"+c.getString(4)+"\talbum_key: "+c.getString(5));
                // Log.d("album",c.getString(3)+"\tart_key:"+c.getString(5));


                /*
                String path = c.getString(0);   // Retrieve path.
                String name = c.getString(1);   // Retrieve name.
                String album = c.getString(2);  // Retrieve album name.
                String artist = c.getString(3); // Retrieve artist name
                */
                // Add the model object to the list .
                musicModel = new MusicModel();



                path = c.getString(0);
                end=path.lastIndexOf('/');
                path=path.substring(0,end);
                musicModel.setTitle(c.getString(1));
                musicModel.setPath(path);
                path=c.getString(0);
                musicModel.setName(c.getString(0).substring(end,path.length()));
                musicModel.setAlbum(c.getString(2));
                musicModel.setArtist(c.getString(3));
                musicModel.setAlbumId(c.getString(4));
                Bitmap bitmap = getAlbumImage(c.getString(0));
                if (bitmap != null) {
                    imageSaver.setFileName("image" + counter+".png").save(bitmap);
                    musicModel.setImage("image" + counter+".png");
                    counter++;
                }else {
                    musicModel.setImage("no");
                }
                musicList.add(musicModel);
                AppDataBase.getInstance(getApplicationContext()).dao().insertMusic(musicModel);

            }
            c.close();

            for( counter=0;counter<musicList.size();counter++){
                    path=musicList.get(counter).getPath();
                    Log.d("test",path);
                if(AppDataBase.getInstance(getApplicationContext()).dao().checkFolderName(path)==null) {
                    end=path.lastIndexOf('/');
                    folderName=path;
                    folderName=path.substring(end+1,folderName.length());
                    folderModel=new FolderModel(folderName,path,musicList.get(counter).getAlbum(),musicList.get(counter).getArtist());
                    AppDataBase.getInstance(getApplicationContext()).dao().insertFolder(folderModel);
                    Log.d("test22",folderName+"\n"+path);
                    folderList.add(folderModel);
                }
            }
        }

    }
    private Bitmap getAlbumImage(String path) {

        byte[] data;
        if(!path.endsWith(".mp3"))
           return null;
        try {
            mmr.setDataSource(path);
            data = mmr.getEmbeddedPicture();
        }catch (Exception e){
            data=null;
        }
        if (data != null){
            return Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(data, 0, data.length),
                    200, 200, false);
        }
        return null;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            }
            getMusicTask();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("برای نصب این برنامه نیاز به اجازه دسترسی به حافظه برای خواند موزیک می باشد.")
                    .setCancelable(true)
            .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   getMusicTask();
                }
            }).setNegativeButton("نه", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
           //this.finish();
        }

        return;
    }
    public void getMusicTask(){
        class  getMusicTask extends AsyncTask<Void,Void,Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                getAllAudioFromDevice();
                if(musicList==null)
                    return 0;
                return 1;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);

                if(result==1) {
                    //folderFragment.setData(folderList);
                    //musicFragment.setData(musicList);
                    Log.d("size2",musicList.size()+"");

                }else {
                    Toast.makeText(getApplicationContext(),"موزیک یافت نشد.",Toast.LENGTH_LONG).show();
                }
            }
        }
        getMusicTask musicTask=new getMusicTask();
        musicTask.execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("pathService",pathMusic);
    }
}

