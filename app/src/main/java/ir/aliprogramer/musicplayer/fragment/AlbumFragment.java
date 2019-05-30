package ir.aliprogramer.musicplayer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.aliprogramer.musicplayer.ItemDecorationAlbumColumns;
import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.adapter.AlbumAdapter;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.MusicModel;

public class AlbumFragment extends Fragment {
    RecyclerView recyclerView;
    List<MusicModel> albumList=new ArrayList<>();
    AlbumAdapter albumAdapter;

    public AlbumFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.album_fragment_layout,container,false);
        recyclerView=view.findViewById(R.id.recycler_music);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        //DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),new LinearLayoutManager(recyclerView.getContext()).getOrientation());
        ItemDecorationAlbumColumns dividerItemDecoration=new ItemDecorationAlbumColumns(3,2);
        recyclerView.addItemDecoration(dividerItemDecoration);
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
                albumList= AppDataBase.getInstance(getContext()).dao().getAllAlbum();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                albumAdapter=new AlbumAdapter(albumList);
                recyclerView.setAdapter(albumAdapter);

                /*for(int i=0;i<albumList.size();i++){
                    Log.d("album8",albumList.get(i).getAlbumId()+": "+albumList.get(i).getAlbum());
                }
                Log.d("album8Size",albumList.size()+"");*/
            }
        }
        getData data=new getData();
        data.execute();
    }

    public void setData(List<MusicModel> albumList) {
        this.albumList=albumList;
        albumAdapter.notifyDataSetChanged();
    }
}
