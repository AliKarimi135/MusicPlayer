package ir.aliprogramer.musicplayer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ir.aliprogramer.musicplayer.R;

import ir.aliprogramer.musicplayer.adapter.FolderAdapter;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.FolderModel;


public class FolderFragment extends Fragment {
    RecyclerView recyclerView;
    List<FolderModel> folderList=new ArrayList<>();
    FolderAdapter folderAdapter;
    public FolderFragment() {
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
        LoadDataFromDb();
    }
    public void LoadDataFromDb(){
        class getData extends AsyncTask<Void,Void,Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                folderList= AppDataBase.getInstance(getContext()).dao().getAllFolder();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                folderAdapter=new FolderAdapter(folderList);
                recyclerView.setAdapter(folderAdapter);
            }
        }
        getData data=new getData();
        data.execute();
    }
    public void setData(List<FolderModel> folderList) {
        this.folderList=folderList;
        folderAdapter.notifyDataSetChanged();
    }
}
