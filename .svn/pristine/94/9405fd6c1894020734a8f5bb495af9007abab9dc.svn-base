package com.eyunda.third.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.client.R;

public class HomepageAdapter extends BaseAdapter {

	private Context context;                        //运行上下文    
    private List<Map<String, Object>> listItems;    //联系人信息集合    
    private LayoutInflater listContainer;           //视图容器    
    private boolean[] hasChecked;                   //记录联系人选中状态    
    public final class ListItemView{                //自定义控件集合      
            public ImageView img;      
            public TextView name;      
            public TextView price; 
            public TextView date; 
      
     }      
    public HomepageAdapter(Context context, List<Map<String, Object>> listItems) {    
        this.context = context;             
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文    
        this.listItems = listItems;    
        hasChecked = new boolean[getCount()];    
    }    
       
    public int getCount() { 
        return listItems.size(); 
    } 

    public Object getItem(int position) { 
        return null; 
    } 

    public long getItemId(int position) { 
        return 0; 
    } 

    public View getView(int position, View convertView, ViewGroup parent) { 
        final int selectID = position; 
        ListItemView  listItemView = null;    
        if (convertView == null) {    
            listItemView = new ListItemView();     
            //获取list_item布局文件的视图    
            convertView = listContainer.inflate(R.layout.home_page_item, null);    
            //获取控件对象    
            listItemView.img = (ImageView)convertView.findViewById(R.id.info_img);    
            listItemView.name = (TextView)convertView.findViewById(R.id.info_name);    
            listItemView.price = (TextView)convertView.findViewById(R.id.info_price);  
            listItemView.date = (TextView)convertView.findViewById(R.id.info_date); 
    
            //设置控件集到convertView    
            convertView.setTag(listItemView);   
               
          //设置联系人信息 
            listItemView.img.setBackgroundResource((Integer) listItems.get(    
                    position).get("img"));   
            listItemView.name.setText((String) listItems.get(    
                    position).get("name"));  
            listItemView.price.setText((String) listItems.get(    
                    position).get("price"));  
            listItemView.date.setText((String) listItems.get(    
                    position).get("date"));  
               
    
        
        }else {    
            listItemView = (ListItemView)convertView.getTag();    
        }  
        return convertView; 
    } 

   
} 

