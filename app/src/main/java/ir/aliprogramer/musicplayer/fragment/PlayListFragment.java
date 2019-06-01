package ir.aliprogramer.musicplayer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ir.aliprogramer.musicplayer.MainActivity;
import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.adapter.MusicAdapter;
import ir.aliprogramer.musicplayer.adapter.PlayListAdapter;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.MusicModel;
import ir.aliprogramer.musicplayer.database.model.MusicModel2;
import ir.aliprogramer.musicplayer.database.model.PlayListModel;
import ir.aliprogramer.musicplayer.touchHelper.EditItemTouchHelperCallback;
import ir.aliprogramer.musicplayer.touchHelper.OnStartDragListener;

public class PlayListFragment extends Fragment implements OnStartDragListener {
    RecyclerView recyclerView;
    List<MusicModel2> musicList2=new ArrayList<>();
    PlayListAdapter playListAdapter;
    ItemTouchHelper mItemTouchHelper;

    public PlayListFragment() {

    }

    public PlayListFragment(List<MusicModel2> musicList) {
        this.musicList2 = musicList;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.playlist_fragment,container,false);
        recyclerView=view.findViewById(R.id.recycler_music);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
       //DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),new LinearLayoutManager(recyclerView.getContext()).getOrientation());
       // recyclerView.addItemDecoration(dividerItemDecoration);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         LoadDataFromDb();
    }
    public void LoadDataFromDb(){

        class getData extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                musicList2= AppDataBase.getInstance(getContext()).dao().getAllPlayList();
                //List<PlayListModel>playList= AppDataBase.getInstance(getContext()).dao().getAllPlayList2();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                load();

            }
        }
        getData data=new getData();
        data.execute();
    }
    public void load(){
            playListAdapter = new PlayListAdapter(musicList2, this);
            ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(playListAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(playListAdapter);

    }
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }
}
