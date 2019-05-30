package ir.aliprogramer.musicplayer.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.aliprogramer.musicplayer.R;
import ir.aliprogramer.musicplayer.database.model.FolderModel;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    List<FolderModel> folderList;

    public FolderAdapter(List<FolderModel> musiclist) {
        this.folderList = musiclist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_folder_row,viewGroup,false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,path;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            path=itemView.findViewById(R.id.path);
        }
    }
}
