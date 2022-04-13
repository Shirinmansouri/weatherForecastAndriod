package com.ms.forecastweather;

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

public class WeatherRvAdapter extends RecyclerView.Adapter<WeatherRvAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherRvModel> weatherRvModelsArrayList;

    public WeatherRvAdapter(Context context, ArrayList<WeatherRvModel> weatherRvModelsArrayList) {
        this.context = context;
        this.weatherRvModelsArrayList = weatherRvModelsArrayList;
    }

    @NonNull
    @Override
    public WeatherRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRvAdapter.ViewHolder holder, int position) {
        WeatherRvModel model=weatherRvModelsArrayList.get(position);
        holder.temperatureTv.setText(model.getTemperature()+" ℃");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.conditionIv);
        holder.windTv.setText(model.getWindSpeed()+"km/h");
        SimpleDateFormat input=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output=new SimpleDateFormat("hh:mm aa");
        try
        {
            Date t=input.parse(model.getTime());
            holder.timeTv.setText(output.format(t));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRvModelsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView windTv,temperatureTv,timeTv;
        ImageView conditionIv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windTv = itemView.findViewById(R.id.IdTvWindSpeed);
            temperatureTv=itemView.findViewById(R.id.IdTvTemperature);
            timeTv =itemView.findViewById(R.id.IdTvTime);
            conditionIv=itemView.findViewById(R.id.IdIvCondition);

        }
    }
}
