package com.example.weatherapp2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterMeteo extends RecyclerView.Adapter<AdapterMeteo.ViewHolder> {
    ArrayList<Meteo> meteo;

    public AdapterMeteo(ArrayList<Meteo> meteos) {
        this.meteo = meteos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_forecast, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.data.setText((String) meteo.get(i).getData());
        viewHolder.meteo.setText((String) meteo.get(i).getMeteo());

        viewHolder.temp.setText((String) meteo.get(i).getTemp());


    }

    @Override
    public int getItemCount() {
        return meteo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView data;
        TextView meteo;
        TextView temp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.temperature);
            data = itemView.findViewById(R.id.date);
            meteo = itemView.findViewById(R.id.meteo);


        }

    }
}
