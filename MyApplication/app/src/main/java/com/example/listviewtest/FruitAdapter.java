package com.example.listviewtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018\3\7 0007.
 */

public class FruitAdapter extends ArrayAdapter<Fruit> {
    private int resourceId;
    public FruitAdapter(Context context, int textViewResourceId, List<Fruit> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Fruit fruit = getItem(position);//获取当前项的Fruit实例
        //只让父布局中声明的layout属性生效，而不为这个
        //View布局添加父布局，因为一旦添加父布局，就不能添加到ListView中了
        View view;
        ViewHolder viewHolder;
        //使用convertView参数将已经加载好的布局进行缓存，以提高ListView的运行效率
        //在快速滚动页面时也能有较好地性能
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            //用findViewById()方法获取ImageView和TextView的实例，然后调用setImageResource()和setText()方法来显示图片和文字
            viewHolder = new ViewHolder();
            viewHolder.fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            viewHolder.fruitName = (TextView) view.findViewById(R.id.fruit_name);
            view.setTag(viewHolder);//将ViewHolder存储在View中
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
        }
        viewHolder.fruitImage.setImageResource(fruit.getImageId());
        viewHolder.fruitName.setText(fruit.getName());
        //最后返回布局
        return view;

    }
    //新建一个内部类ViewHolder，用于对控件的实例进行缓存
    class ViewHolder {
        ImageView fruitImage;
        TextView fruitName;
    }
}
