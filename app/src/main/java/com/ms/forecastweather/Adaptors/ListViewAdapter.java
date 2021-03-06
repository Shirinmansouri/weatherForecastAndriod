package com.ms.forecastweather.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ms.forecastweather.R;
import com.ms.forecastweather.Models.WeatherModel;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<WeatherModel> {
    private static final String TAG="PersonListAdapter";
    private Context mContext;
    private int mResource;

    public ListViewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<WeatherModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String cityName = getItem(position).getCityName();
        String searchDate = getItem(position).getSearchDate();
        String searchTemperature = getItem(position).getTemperature();
        String searchWindSpeed = getItem(position).getWindSpeed();
        String searchTime = getItem(position).getTime();
        WeatherModel _weatherModel = new WeatherModel(searchDate, cityName, searchTime, searchTemperature, "", searchWindSpeed);
        LayoutInflater inflator= LayoutInflater.from(mContext);
        convertView=inflator.inflate(mResource,parent,false);

        TextView tvCity=(TextView) convertView.findViewById(R.id.IdTvSearchCityName);
        TextView tvDate=(TextView) convertView.findViewById(R.id.IdTvSearchDate);


        TextView tvTime=(TextView) convertView.findViewById(R.id.IdTvSearchTime);
        TextView tvTemperature=(TextView) convertView.findViewById(R.id.IdTvSearchTemperature);
        TextView tvWind=(TextView) convertView.findViewById(R.id.IdTvSearchWindSpeed);
        tvCity.setText(cityName);
        DateFormat _date = new SimpleDateFormat("MM/dd/yy");
        String strDate = _date.format(Date.parse(searchDate));
        tvDate.setText(strDate);
        tvTime.setText(searchTime.substring(11, 16));
        tvTemperature.setText(searchTemperature);
        tvWind.setText(searchWindSpeed);
        return  convertView;
    }
    }