package com.example.recyclerviewtest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018\3\7 0007.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private List<Fruit> mFruitList;
    static public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView fruitImage;
        TextView fruitName;
        public ViewHolder(View view) {
            super(view);
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }
    }
    /****构造函数，将要展示的数据源传进来，并赋值给一个全局变量mFruitList****/
    public FruitAdapter(List<Fruit> fruitList){
        mFruitList = fruitList;
    }
    @Override
    /**** 用于创建ViewHolder实例 ****/
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        将fruit_item布局加载进来
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
//        创建一个ViewHolder实例，并将加载的布局传入到构造函数中
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    /**** 将RecyclerView子项的数据进行赋值，每当每个子项滚动到屏幕内执行，
     * 通过position定位到当前Fruit实例，
     * 再将数据设置到ViewHolder的FruitImage和FruitName
     * ****/
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
    }

    @Override
    /**** f返回mFruitList子项的数目****/
    public int getItemCount() {
        return mFruitList.size();
    }


}
