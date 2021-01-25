package com.example.motoapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.R;

import java.util.List;

public class RecyclerAdapterExtendedTriggers extends RecyclerView.Adapter<RecyclerAdapterExtendedTriggers.LocalViewHolderExtendedTrigger> {

    List<Integer> dataIndexes;
    List<String> whatToDos;
    List<Integer> mths;
    List<Integer> dones;

    Context context;
    RecyclerViewClickListner recyclerViewClickListner;
    DatabaseAdapter adapter;
    int idx;

    public RecyclerAdapterExtendedTriggers(
            Context ct,
            int vehicle,
            List<Integer> dataIndexes,
            List<String> whatToDos,
            List<Integer> mths,
            List<Integer> dones,
            RecyclerViewClickListner recyclerViewClickListner) {
        this.context = ct;
        this.idx = vehicle;
        this.adapter = new DatabaseAdapter(this.context);
        this.dataIndexes = dataIndexes;
        this.whatToDos = whatToDos;
        this.mths = mths;
        this.dones = dones;
        this.recyclerViewClickListner = recyclerViewClickListner;
    }

    public void updateAdapter()
    {
        DatabaseAdapter.TriggersAnswer answer = adapter.getTriggers(this.idx);
        this.dataIndexes = answer.indexes;
        this.whatToDos = answer.whatToDo;
        this.mths = answer.mth;
        this.dones = answer.done;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocalViewHolderExtendedTrigger onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewExtended = inflater.inflate(R.layout.row_service_template_extended_triggers, parent, false);
        return new LocalViewHolderExtendedTrigger(viewExtended);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalViewHolderExtendedTrigger holder, int position) {
        holder.stringNameVarExtended.setText(whatToDos.get(position));
        holder.stringSubnameVarExtended.setText("mth: " + String.valueOf(mths.get(position)));

        holder.buttonDeleteHandler.setVisibility(View.GONE);

        holder.buttonDeleteHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.triggerCompleted(dataIndexes.get(position));
                updateAdapter();
            }
        });

        holder.expandedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.buttonDeleteHandler.getVisibility() == View.GONE) {
                    holder.buttonDeleteHandler.setVisibility(View.VISIBLE);
                } else {
                    holder.buttonDeleteHandler.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataIndexes.size();
    }

    public class LocalViewHolderExtendedTrigger extends RecyclerView.ViewHolder{

        public View expandedView;
        TextView stringNameVarExtended;
        TextView stringSubnameVarExtended;
        Button buttonDeleteHandler;

        public LocalViewHolderExtendedTrigger(@NonNull View itemViewExtended) {
            super(itemViewExtended);
            this.expandedView = itemViewExtended;

            stringNameVarExtended = itemViewExtended.findViewById(R.id.textViewNameExtendedTrigger);
            stringSubnameVarExtended = itemViewExtended.findViewById(R.id.textViewSubnameExtendedTrigger);

            buttonDeleteHandler = itemViewExtended.findViewById(R.id.buttonTrigger);

            buttonDeleteHandler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ERASE: ", "this trigger");
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickListner.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
