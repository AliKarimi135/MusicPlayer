package ir.aliprogramer.musicplayer.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.database.AppDataBase;
import ir.aliprogramer.musicplayer.database.model.FolderModel;
import ir.aliprogramer.musicplayer.database.model.MusicModel;
import ir.aliprogramer.musicplayer.fragment.MusicFragment;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    List<FolderModel> folderList;
    Context context;
    List<MusicModel>musicList;
    FragmentManager manager;

    public FolderAdapter(List<FolderModel> musiclist, FragmentManager manager) {
        this.folderList = musiclist;
        this.manager=manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_folder_row,viewGroup,false);
        context=viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
         viewHolder.name.setText(folderList.get(i).getName());
         viewHolder.path.setText(folderList.get(i).getPath());
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,path;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            path=itemView.findViewById(R.id.path);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            getMusicListByPath(folderList.get(getAdapterPosition()).getPath());
        }
    }

    private void getMusicListByPath(final String path) {

        class getMusicListByPath extends AsyncTask<Void,Void,Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                musicList= AppDataBase.getInstance(context).dao().getMusicListForFolder(path);
                if(musicList==null)
                    return 0;
                return 1;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if(result==0){
                    Toast.makeText(context,"در این پوشه موزیکی وجود ندارد",Toast.LENGTH_LONG).show();
                }else{
                    MusicFragment musicFragment=new MusicFragment(musicList,1);
                    FragmentTransaction transaction=manager.beginTransaction();
                    //transaction.show(musicFragment);
                    transaction.add(R.id.frame_layout,musicFragment,"musicFragment2");
                    transaction.addToBackStack("musicFragment2");
                    transaction.commit();
                }
            }
        }
        getMusicListByPath listByPath=new getMusicListByPath();
        listByPath.execute();
    }
}
