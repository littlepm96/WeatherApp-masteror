package com.example.weatherapp2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdapterPreferiti extends RecyclerView.Adapter<AdapterPreferiti.ViewHolder> {
    private ArrayList<String> strings;

    public AdapterPreferiti(ArrayList<String> cities) {
        this.strings = cities;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_list, viewGroup, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.titolo.setText((String) strings.get(i));

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titolo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titolo = itemView.findViewById(R.id.nomeCity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String text = strings.get(getAdapterPosition());
                    Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public interface OnBtnClickListner {
        void onDeleteBtnClick(int position);
    }

}