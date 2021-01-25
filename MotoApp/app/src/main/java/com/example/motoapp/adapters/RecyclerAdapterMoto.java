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

public class RecyclerAdapterMoto extends RecyclerView.Adapter<RecyclerAdapterMoto.LocalViewHolder> {

    List<String> data;
    List<Integer> mths;
    Context context;
    RecyclerViewClickListner recyclerViewClickListner;

    public RecyclerAdapterMoto(Context ct, List<String> tempData, List<Integer> mths, RecyclerViewClickListner recyclerViewClickListner) {
        this.context = ct;
        this.data = tempData;
        this.mths = mths;
        this.recyclerViewClickListner = recyclerViewClickListner;
    }

    public void updateAdapter(List<String> newList, List<Integer> newMths)
    {
        this.mths = newMths;
        this.data = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_moto_template, parent, false);
        return new LocalViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LocalViewHolder holder, int position) {
        holder.stringNameVar.setText(data.get(position));
        if (this.mths != null) holder.stringSubnameVar.setText("mth: " + String.valueOf(mths.get(position)));
        else holder.stringSubnameVar.setText("Available");
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
