package ir.aliprogramer.musicplayer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.aliprogramer.musicplayer.MainActivity;
import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.adapter.MusicAdapter;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.MusicModel;



public class MusicFragment extends Fragment {
    RecyclerView recyclerView;
    List<MusicModel> musicList=new ArrayList<>();
    MusicAdapter musicAdapter;
    boolean initList=true;
    int status=0;
    int end;
    String title;
    public MusicFragment( ) {
    }
    public MusicFragment(List<MusicModel> musicList,int status) {
        this.musicList=musicList;
        Log.d("list2",musicList.size()+"");
        initList=false;
        this.status=status;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          View view=inflater.inflate(R.layout.music_fragment_layout,container,false);
          recyclerView=view.findViewById(R.id.recycler_music);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),new LinearLayoutManager(recyclerView.getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(initList)
            LoadDataFromDb();
        else {
            //set title toolbar
            if(status==1){
                title=musicList.get(0).getPath();
                end=title.lastIndexOf('/');
                title=title.substring(end+1,musicList.get(0).getPath().length());
            }else if(status==2){
                title=musicList.get(0).getAlbum();
            }else {
                title=musicList.get(0).getArtist();
             }
            ((MainActivity) getContext()).showFrameLayout(title);
            musicAdapter=new MusicAdapter(musicList);
            recyclerView.setAdapter(musicAdapter);

        }

    }
    public void LoadDataFromDb(){

        class getData extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                musicList= AppDataBase.getInstance(getContext()).dao().getAllMusic();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                musicAdapter=new MusicAdapter(musicList);
                recyclerView.setAdapter(musicAdapter);
            }
        }
        getData data=new getData();
        data.execute();
    }

    public void setData(List<MusicModel> musicList) {
        this.musicList=musicList;
        musicAdapter.notifyDataSetChanged();
    }
}
