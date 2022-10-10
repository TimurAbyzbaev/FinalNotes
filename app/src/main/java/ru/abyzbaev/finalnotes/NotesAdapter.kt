package ru.abyzbaev.finalnotes;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    private OnItemClickListener itemClickListener;
    //private ArrayList<Node> data;
    private NodeListSource dataSource;

    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public NotesAdapter() {
        //this.data = data;

    }

    /*public void setData(ArrayList<Node> data) {
        this.data = data;
        notifyDataSetChanged();
    }*/

    /*public ArrayList<Node> getData() {
        return data;
    }*/

    public void setDataSource(NodeListSource dataSource){
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTitle().setText(dataSource.getNodeList().get(position).getTitle().toString());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;

        public ViewHolder(View view){
            super(view);
            //title = (TextView) view.findViewById(R.id.note_title);
            title = view.findViewById(R.id.note_title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(itemClickListener != null){
                        itemClickListener.onItemClick(v,position);
                    }
                }
            });
            title.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(itemClickListener != null){
                        itemClickListener.onItemLongClick(v,position);
                    }
                    return true;
                }
            });
        }

        public TextView getTitle(){
            return title;
        }
    }
}
