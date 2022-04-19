package com.ms.forecastweather;

import com.ms.forecastweather.Models.WeatherModel;

import java.util.ArrayList;

public class Paginator {
    public static  int TOTAL_NUM_ITEM=10;
    public static  int ITEM_PER_PAGE=10;
    public static  int ITEM_REMAINING=TOTAL_NUM_ITEM%ITEM_PER_PAGE;
    public static int LAST_PAGE=TOTAL_NUM_ITEM/ITEM_PER_PAGE;
    public static ArrayList<WeatherModel> realData;
    public ArrayList<WeatherModel> generatePage(int currentPage)
    {
        int startItem=currentPage*ITEM_PER_PAGE+1;
        int numOfData=ITEM_PER_PAGE;
        ArrayList<WeatherModel> pageDate=new ArrayList<WeatherModel>();
        if (currentPage==LAST_PAGE && ITEM_REMAINING>0)
        {
            for (int i=startItem ; i<startItem+ITEM_REMAINING ;i++ )
            {
                pageDate.add(realData.get(i));
            }
        }
        else if (currentPage>0)
        {
            for (int i=startItem ; i<startItem+numOfData ;i++ )
            {
                pageDate.add(realData.get(i));
            }
        }
        else
        {
            for (int i=0 ; i<numOfData ;i++ )
            {
                pageDate.add(realData.get(i));
            }
        }
        return pageDate;
    }
}


