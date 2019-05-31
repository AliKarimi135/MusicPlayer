package ir.aliprogramer.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.appcompat.widget.AppCompatImageView;
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

import ir.aliprogramer.musicplayer.ImageSaver;
import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.ArtistModel;
import ir.aliprogramer.musicplayer.database.model.MusicModel;
import ir.aliprogramer.musicplayer.fragment.MusicFragment;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    List<ArtistModel>artistList;
    String imagePath,name;
    Context context;
    ImageSaver imageSaver;
    List<MusicModel>musicList;
    FragmentManager manager;
    public ArtistAdapter(List<ArtistModel> artistList, FragmentManager manager) {
        this.artistList = artistList;
        this.manager=manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_artist_row,viewGroup,false);
        context=viewGroup.getContext();
        imageSaver=new ImageSaver(context).setDirectoryName("musicImageDir").setExternal(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        name=artistList.get(i).getName();
        if(name.length()>25) {
            name=name.substring(0,24);
        }
        viewHolder.artist.setText(name);
        viewHolder.numberAlbum.setText(artistList.get(i).getNumberAlbum()+"");
        viewHolder.numberTrack.setText(artistList.get(i).getNumberTrack()+"");

            imagePath=artistList.get(i).getImage();
            if(!imagePath.equals("no")) {
                Bitmap bitmap = imageSaver.setFileName(imagePath).load();
                Glide.with(context).load(bitmap).into(viewHolder.image);
            }else{
                 viewHolder.image.setBackgroundColor(Color.GREEN);
            }
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView artist,numberAlbum,numberTrack;
        AppCompatImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artist=itemView.findViewById(R.id.artist);
            numberAlbum=itemView.findViewById(R.id.number_album);
            numberTrack=itemView.findViewById(R.id.number_track);
            image=itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            getMusicListForArtist(artistList.get(getAdapterPosition()).getName());

        }
    }
    private void getMusicListForArtist(final String artistName) {
        class getMusicListForArtist extends AsyncTask<Void,Void,Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                musicList= AppDataBase.getInstance(context).dao().getMusicListForArtist(artistName);
                if(musicList==null)
                    return 0;
                return 1;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if(result==0){
                    Toast.makeText(context,"برای این هنرمند موزیکی وجود ندارد",Toast.LENGTH_LONG).show();
                }else{
                    MusicFragment musicFragment=new MusicFragment(musicList,3);
                    FragmentTransaction transaction=manager.beginTransaction();
                    //transaction.show(musicFragment);
                    transaction.add(R.id.frame_layout,musicFragment,"musicFragment4");
                    transaction.addToBackStack("musicFragment4");
                    transaction.commit();
                }
            }
        }
        getMusicListForArtist listByPath=new getMusicListForArtist();
        listByPath.execute();
    }
}
