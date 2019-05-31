package ir.aliprogramer.musicplayer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.adapter.ArtistAdapter;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.ArtistModel;

public class ArtistFragment extends Fragment {
    RecyclerView recyclerView;
    ArtistAdapter artistAdapter;
    List<ArtistModel>artistList=new ArrayList<>();
    FragmentManager manager;
    public ArtistFragment() {
    }

    public ArtistFragment(FragmentManager manager) {
        this.manager = manager;
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
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),new LinearLayoutManager(recyclerView.getContext()).getOrientation());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoadData();
    }

    private void LoadData() {
        class LoadData extends AsyncTask<Void,Void, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                artistList= AppDataBase.getInstance(getContext()).dao().getAllArtist();
                if(artistList==null)
                    return 0;
                return 1;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                //if(result==1){
                    artistAdapter=new ArtistAdapter(artistList,manager);
                    recyclerView.setAdapter(artistAdapter);
                Log.d("artist",artistList.get(0).getName()+"");
               // }
            }
        }
        LoadData loadData=new LoadData();
        loadData.execute();
    }

}
