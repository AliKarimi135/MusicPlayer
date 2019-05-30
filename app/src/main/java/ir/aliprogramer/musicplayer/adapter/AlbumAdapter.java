package ir.aliprogramer.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.aliprogramer.musicplayer.ImageSaver;
import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.database.model.MusicModel;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    List<MusicModel> albumList;
    Context context;
    String albumName,Artist,imagePath;
    ImageSaver imageSaver;
    public AlbumAdapter(List<MusicModel> albumList) {
        this.albumList = albumList;
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
        }else{
           // viewHolder.image.setBackgroundColor(Color.GREEN);
        }
        /*Bitmap bitmap=getAlbumImage(albumList.get(i).getPath()+albumList.get(i).getName());
        if(bitmap==null){
            //Glide.with(context).load(R.drawable.defult).into(viewHolder.image);
          //  viewHolder.image.setImageResource(R.drawable.defult);
        }else{
            Glide.with(context).load(bitmap).into(viewHolder.image);
            //viewHolder.image.setImageBitmap(bitmap);
        }*/
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image;
        TextView album,artist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.album_img);
            album=itemView.findViewById(R.id.album_name);
            artist=itemView.findViewById(R.id.album_artist);
        }
    }
    private Bitmap getAlbumImage(String path) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] data;
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
}
