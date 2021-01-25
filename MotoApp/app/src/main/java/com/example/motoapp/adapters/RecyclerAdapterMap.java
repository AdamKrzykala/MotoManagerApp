package com.example.motoapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.R;

import java.util.List;

public class RecyclerAdapterMap extends RecyclerView.Adapter<RecyclerAdapterMap.LocalViewHolderMap> {

    List<String> data;
    List<Integer> mths;
    Context context;
    RecyclerViewClickListner recyclerViewClickListner;

    public RecyclerAdapterMap(Context ct, List<String> tempData, RecyclerViewClickListner recyclerViewClickListner) {
        this.context = ct;
        this.data = tempData;
        this.recyclerViewClickListner = recyclerViewClickListner;
    }

    public void updateAdapter(List<String> newList)
    {
        this.data = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocalViewHolderMap onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_map_template, parent, false);
        return new LocalViewHolderMap(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LocalViewHolderMap holder, int position) {
        holder.stringNameVar.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class LocalViewHolderMap extends RecyclerView.ViewHolder{

        TextView stringNameVar;

        public LocalViewHolderMap(@NonNull View itemView) {
            super(itemView);
            stringNameVar = itemView.findViewById(R.id.textViewTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickListner.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
