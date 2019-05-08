package com.eyunda.part1.plug;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hy.client.R;

public class PageAdapter extends BaseAdapter {
    private List<Img> list;
    LayoutInflater inflater;
    public PageAdapter(Context context,List<Img> list) {
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CacheView cacheView;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.plug_item_page, null);
            cacheView=new CacheView();
            cacheView.tv_des=(TextView) convertView.findViewById(R.id.tv_des);
            //cacheView.imgv_img=(ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(cacheView);
        }else{
            cacheView=(CacheView) convertView.getTag();
        }
        cacheView.tv_des.setText(list.get(position).getDes());
        
        return convertView;
    }
    
    private static class CacheView{
        TextView tv_des;
        //ImageView imgv_img;
    }
}