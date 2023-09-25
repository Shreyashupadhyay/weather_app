package com.example.waetherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.viewHolder> {
    private Context context;
    private ArrayList<Weathermodel> Weathermodel;

    public WeatherAdapter(Context context, ArrayList<com.example.waetherapp.Weathermodel> weathermodel) {
        this.context = context;
        Weathermodel = weathermodel;
    }

    @NonNull
    @Override
    public WeatherAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.viewHolder holder, int position) {
        Weathermodel model = Weathermodel.get(position);
        holder.tempratureTV.setText(model.getTemperature()+"°C");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.condition);
        holder.windTV.setText(model.getWindspeed()+"Km/h");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try{
            Date t = input.parse(model.getTime());
            holder.timeTV.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return Weathermodel.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private TextView windTV , tempratureTV, timeTV;
        private ImageView condition;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            windTV = itemView.findViewById(R.id.idTVWindspeed);
            tempratureTV = itemView.findViewById(R.id.idTVTempra);
            timeTV = itemView.findViewById(R.id.idTVtime);
            condition = itemView.findViewById(R.id.idTVCondition);


        }
    }
}
