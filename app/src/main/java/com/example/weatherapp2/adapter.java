package com.example.weatherapp2;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder>{
private ArrayList<String> strings;

    public adapter(ArrayList<String> cities) {
        this.strings=cities;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_list,viewGroup,false);

                return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.titolo.setText((String)strings.get(i));

    }


    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titolo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        titolo= itemView.findViewById(R.id.title);

    }

        }
}






