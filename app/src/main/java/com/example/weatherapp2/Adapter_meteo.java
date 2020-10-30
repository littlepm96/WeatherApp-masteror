package com.example.weatherapp2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_meteo extends RecyclerView.Adapter<Adapter_meteo.ViewHolder> {
    ArrayList<Meteo> meteo;
    public Adapter_meteo() {
    }

    public Adapter_meteo(ArrayList<Meteo> meteos) {
        this.meteo=meteos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_w,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.data.setText((String)meteo.get(i).getData());
        viewHolder.meteo.setText((String)meteo.get(i).getMeteo());

        viewHolder.temp.setText((String)meteo.get(i).getTemp());


    }

    @Override
    public int getItemCount() {
        return meteo.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView data;TextView meteo;TextView temp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temp=itemView.findViewById(R.id.temp);
            data= itemView.findViewById(R.id.data);
            meteo= itemView.findViewById(R.id.meteo);


        }

    }
}
