package ir.aliprogramer.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ir.aliprogramer.musicplayer.ImageSaver;
import ir.aliprogramer.musicplayer.MainActivity;
import ir.aliprogramer.musicplayer.database.model.MusicModel;
import ir.aliprogramer.musicplayer.R;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    List<MusicModel>musiclist;
    Context context;
    String title,Artist,imagePath;
    ImageSaver imageSaver;
    public MusicAdapter(List<MusicModel> modelList) {
        this.musiclist = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_music_row,viewGroup,false);
        context=viewGroup.getContext();
        imageSaver=new ImageSaver(context).setDirectoryName("musicImageDir").setExternal(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        title=musiclist.get(i).getTitle();
        Artist=musiclist.get(i).getArtist();
        if(title.length()>30)
            title=title.substring(0,28);
        if(Artist.length()>30)
            Artist=Artist.substring(0,28);
        viewHolder.name.setText(title);
        viewHolder.artist.setText(Artist);

        imagePath=musiclist.get(i).getImage();
        if(!imagePath.equals("no")) {
            Bitmap bitmap = imageSaver.setFileName(imagePath).load();
            Log.d("bitmap",imagePath);
            Glide.with(context).load(bitmap).into(viewHolder.image);
        }else{
            viewHolder.image.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return musiclist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,artist;
        AppCompatImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            artist=itemView.findViewById(R.id.artist);
            image=itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("play1",musiclist.get(getAdapterPosition()).getPath());
            ((MainActivity)context).play(musiclist.get(getAdapterPosition()).getPath()+musiclist.get(getAdapterPosition()).getName());
        }
    }
    private Bitmap getAlbumImage(String path) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null){
            return Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(data, 0, data.length),
                    55, 55, false);
        }
        return null;
    }
}
