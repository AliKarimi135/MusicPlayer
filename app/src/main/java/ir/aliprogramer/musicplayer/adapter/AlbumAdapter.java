package ir.aliprogramer.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.aliprogramer.musicplayer.ImageSaver;
import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.MusicModel;
import ir.aliprogramer.musicplayer.fragment.MusicFragment;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    List<MusicModel> albumList;
    Context context;
    String albumName,Artist,imagePath;
    ImageSaver imageSaver;
    List<MusicModel>musicList;
    FragmentManager manager;

    public AlbumAdapter(List<MusicModel> albumList, FragmentManager manager) {
        this.albumList = albumList;
        this.manager=manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_album_row,viewGroup,false);
        context=viewGroup.getContext();
        imageSaver=new ImageSaver(context).setDirectoryName("musicImageDir").setExternal(true);
        return new AlbumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        albumName=albumList.get(i).getAlbum();
        Artist=albumList.get(i).getArtist();
        if(albumName.length()>28)
            albumName=albumName.substring(0,24);
        if(Artist.length()>28)
            Artist=Artist.substring(0,24);
        viewHolder.album.setText(albumName);
        viewHolder.artist.setText(Artist);
        imagePath=albumList.get(i).getImage();
        if(!imagePath.equals("no")) {
            Bitmap bitmap = imageSaver.setFileName(imagePath).load();
            Glide.with(context).load(bitmap).into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView image;
        TextView album,artist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.album_img);
            album=itemView.findViewById(R.id.album_name);
            artist=itemView.findViewById(R.id.album_artist);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            getMusicListForAlbum(albumList.get(getAdapterPosition()).getPath(),albumList.get(getAdapterPosition()).getAlbumId());

        }
    }

    private void getMusicListForAlbum(final String path,final String albumId) {
        class getMusicListForAlbum extends AsyncTask<Void,Void,Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                musicList= AppDataBase.getInstance(context).dao().getMusicListForAlbum(path,albumId);
                if(musicList==null)
                    return 0;
                return 1;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if(result==0){
                    Toast.makeText(context,"در این آلبوم موزیکی وجود ندارد",Toast.LENGTH_LONG).show();
                }else{
                    MusicFragment musicFragment=new MusicFragment(musicList,2);
                    FragmentTransaction transaction=manager.beginTransaction();
                    //transaction.show(musicFragment);
                    transaction.add(R.id.frame_layout,musicFragment,"musicFragment3");
                    transaction.addToBackStack("musicFragment3");
                    transaction.commit();
                }
            }
        }
        getMusicListForAlbum listByPath=new getMusicListForAlbum();
        listByPath.execute();
    }
    }

