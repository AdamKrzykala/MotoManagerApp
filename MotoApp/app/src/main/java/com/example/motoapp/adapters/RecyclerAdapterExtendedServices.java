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

public class RecyclerAdapterExtendedServices extends RecyclerView.Adapter<RecyclerAdapterExtendedServices.LocalViewHolderExtendedService> {

    List<Integer> dataIndexes;
    List<String> dataNames;
    List<String> dataSubnames;
    List<String> descriptions;
    Context context;
    RecyclerViewClickListner recyclerViewClickListner;
    DatabaseAdapter adapter;
    int idx;

    public RecyclerAdapterExtendedServices(
            Context ct,
            int vehicle,
            List<Integer> dataIndexes,
            List<String> dataNames,
            List<String> dataSubnames,
            List<String> descriptions,
            RecyclerViewClickListner recyclerViewClickListner) {
        this.context = ct;
        this.idx = vehicle;
        this.adapter = new DatabaseAdapter(this.context);
        this.dataIndexes = dataIndexes;
        this.dataNames = dataNames;
        this.dataSubnames = dataSubnames;
        this.descriptions = descriptions;
        this.recyclerViewClickListner = recyclerViewClickListner;
    }

    public void updateAdapter()
    {
        DatabaseAdapter.ServicesAnswer answer = adapter.getServices(this.idx);
        this.dataIndexes = answer.indexes;
        this.dataNames = answer.names;
        this.dataSubnames = answer.dates;
        this.descriptions = answer.descriptions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LocalViewHolderExtendedService onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewExtended = inflater.inflate(R.layout.row_service_template_extended_services, parent, false);
        return new LocalViewHolderExtendedService(viewExtended);
    }


    @Override
    public void onBindViewHolder(@NonNull LocalViewHolderExtendedService holder, int position) {
        holder.stringNameVarExtended.setText(dataNames.get(position));
        holder.stringSubnameVarExtended.setText(dataSubnames.get(position));
        holder.stringDescriptionExtended.setText(descriptions.get(position));
        holder.stringDescriptionExtended.setVisibility(View.GONE);
        holder.buttonDeleteHandler.setVisibility(View.GONE);

        holder.buttonDeleteHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.deleteService(dataIndexes.get(position));
                updateAdapter();
            }
        });

        holder.expandedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.buttonDeleteHandler.getVisibility() == View.GONE) {
                    holder.stringDescriptionExtended.setVisibility(View.VISIBLE);
                    holder.buttonDeleteHandler.setVisibility(View.VISIBLE);
                } else {
                    holder.stringDescriptionExtended.setVisibility(View.GONE);
                    holder.buttonDeleteHandler.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataNames.size();
    }

    public class LocalViewHolderExtendedService extends RecyclerView.ViewHolder{

        public View expandedView;
        TextView stringNameVarExtended;
        TextView stringSubnameVarExtended;
        TextView stringDescriptionExtended;
        Button buttonDeleteHandler;

        public LocalViewHolderExtendedService(@NonNull View itemViewExtended) {
            super(itemViewExtended);
            this.expandedView = itemViewExtended;

            stringNameVarExtended = itemViewExtended.findViewById(R.id.textViewNameExtended);
            stringSubnameVarExtended = itemViewExtended.findViewById(R.id.textViewSubnameExtended);
            stringDescriptionExtended = itemViewExtended.findViewById(R.id.textViewDescriptionExtended);
            buttonDeleteHandler = itemViewExtended.findViewById(R.id.button);

            buttonDeleteHandler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("ERASE: ", "this service");
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickListner.onItemClick(getAdapterPosition());
                }
            });
        }
        public View getExpandedView()
        {
            return this.expandedView;
        }
    }
}
