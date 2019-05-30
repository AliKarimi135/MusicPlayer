package ir.aliprogramer.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ir.aliprogramer.musicplayer.ImageSaver;
import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.database.model.ArtistModel;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    List<ArtistModel>artistList;
    String imagePath,name;
    Context context;
    ImageSaver imageSaver;
    public ArtistAdapter(List<ArtistModel> artistList) {
        this.artistList = artistList;
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView artist,numberAlbum,numberTrack;
        AppCompatImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            artist=itemView.findViewById(R.id.artist);
            numberAlbum=itemView.findViewById(R.id.number_album);
            numberTrack=itemView.findViewById(R.id.number_track);
            image=itemView.findViewById(R.id.img);
        }
    }
}
