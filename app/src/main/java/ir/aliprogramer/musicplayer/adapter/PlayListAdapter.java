package ir.aliprogramer.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import ir.aliprogramer.musicplayer.ImageSaver;
import ir.aliprogramer.musicplayer.MainActivity;
import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.MusicModel;
import ir.aliprogramer.musicplayer.database.model.MusicModel2;
import ir.aliprogramer.musicplayer.database.model.PlayListModel;
import ir.aliprogramer.musicplayer.touchHelper.ItemTouchHelperAdapter;
import ir.aliprogramer.musicplayer.touchHelper.ItemTouchHelperViewHolder;
import ir.aliprogramer.musicplayer.touchHelper.OnStartDragListener;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    List<MusicModel2> musiclist;
    Context context;
    String title,Artist,imagePath;
    ImageSaver imageSaver;
   OnItemClickListener mItemClickListener;


    private final OnStartDragListener mDragStartListener;

    public PlayListAdapter(List<MusicModel2> musiclist, OnStartDragListener mDragStartListener) {
        this.musiclist = musiclist;
        this.mDragStartListener = mDragStartListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_music_row,parent,false);
        context=parent.getContext();
        imageSaver=new ImageSaver(context).setDirectoryName("musicImageDir").setExternal(true);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        title=musiclist.get(position).getTitle();
        Artist=musiclist.get(position).getArtist();
        if(title.length()>30)
            title=title.substring(0,28);
        if(Artist.length()>30)
            Artist=Artist.substring(0,28);
        holder.name.setText(title);
        holder.artist.setText(Artist);

        imagePath=musiclist.get(position).getImage();
        if(!imagePath.equals("no")) {
            Bitmap bitmap = imageSaver.setFileName(imagePath).load();
            Log.d("bitmap",imagePath);
            Glide.with(context).load(bitmap).into(holder.image);
        }else{
            holder.image.setBackgroundColor(Color.GREEN);
        }
    holder.name.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                mDragStartListener.onStartDrag(holder);
            }
            return false;
        }
    });
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    @Override
    public int getItemCount() {
        return musiclist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder, View.OnLongClickListener {
        TextView name,artist;
        AppCompatImageView image;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            artist=itemView.findViewById(R.id.artist);
            image=itemView.findViewById(R.id.img);
            linearLayout=itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            ((MainActivity)context).play(musiclist.get(getAdapterPosition()).getPath()+musiclist.get(getAdapterPosition()).getName());

        }


        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }

        @Override
        public boolean onLongClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
            return true;
        }
    }
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            //Log.v("", "Log position" + fromPosition + " " + toPosition);
            if (fromPosition < musiclist.size() && toPosition < musiclist.size()) {
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(musiclist, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(musiclist, i, i - 1);
                    }
                }
                changePositonInDB(fromPosition,toPosition);
                notifyItemMoved(fromPosition, toPosition);
            }
            return true;
        }

    private void changePositonInDB(final int fromPosition, final int toPosition) {
        class  ChangePositonInDB extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDataBase.getInstance(context).dao().updatePlayList(musiclist.get(fromPosition).getId(),toPosition);
                AppDataBase.getInstance(context).dao().updatePlayList(musiclist.get(toPosition).getId(),fromPosition);
                return null;
            }
        }
        ChangePositonInDB changePositonInDB=new ChangePositonInDB();
        changePositonInDB.execute();
    }

    @Override
        public void onItemDismiss(int position) {
            deleteItemInDB(musiclist.get(position).getId());
            musiclist.remove(position);
            notifyItemRemoved(position);
        }
    private void deleteItemInDB(final int musicId) {
        class  DeleteItemInDB extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDataBase.getInstance(context).dao().deletePlayList(musicId);
                return null;
            }
        }
        DeleteItemInDB deleteItemInDB=new DeleteItemInDB();
        deleteItemInDB.execute();
    }
    public void updateList(List<MusicModel2> list) {
        musiclist = list;
        notifyDataSetChanged();
    }
}
