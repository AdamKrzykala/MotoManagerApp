package com.example.motoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LocalViewHolder> {

    List<String> data;
    Context context;
    RecyclerViewClickListner recyclerViewClickListner;

    public RecyclerAdapter(Context ct, List<String> tempData, RecyclerViewClickListner recyclerViewClickListner) {
        this.context = ct;
        this.data = tempData;
        this.recyclerViewClickListner = recyclerViewClickListner;
    }

    @NonNull
    @Override
    public LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_template, parent, false);
        return new LocalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalViewHolder holder, int position) {
        holder.stringNameVar.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class LocalViewHolder extends RecyclerView.ViewHolder{

        TextView stringNameVar;
        TextView stringSubnameVar;

        public LocalViewHolder(@NonNull View itemView) {
            super(itemView);
            stringNameVar = itemView.findViewById(R.id.textViewName);
            stringSubnameVar = itemView.findViewById(R.id.textViewSubname);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickListner.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
