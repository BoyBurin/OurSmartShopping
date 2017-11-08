package com.example.seniorproject.smartshopping.view.recyclerviewadapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.model.dao.group.Group;
import com.example.seniorproject.smartshopping.model.dao.group.GroupList;
import com.example.seniorproject.smartshopping.view.transformation.CircleTransform;

import java.util.ArrayList;

/**
 * Created by boyburin on 11/8/2017 AD.
 */

public class SelectedGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<GroupList> groups;
    private Context context;

    private OnItemClickListener listener;

    /***********************************************************************************************
     *************************************Interface ********************************************
     ***********************************************************************************************/

    public interface OnItemClickListener {
        void onItemClick(GroupList group);
    }


    /***********************************************************************************************
     ************************************* Method ********************************************
     ***********************************************************************************************/

    public SelectedGroupAdapter(Context context){
        this.context = context;
    }

    public void setGroups(ArrayList<GroupList> groups){
        this.groups = groups;
    }

    public void setItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_view_group_selected_group, parent, false);

        int width = parent.getMeasuredWidth();
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;


        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();

        params.width = width/2;
        params.height = height/2;
        Log.d("Height", height + "");

        view.setLayoutParams(params);

        RecyclerView.ViewHolder viewHolder = new SelectedGroupViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final GroupList group = groups.get(position);

        SelectedGroupViewHolder selectedGroupViewHolder = (SelectedGroupViewHolder)holder;
        selectedGroupViewHolder.tvGroupName.setText(group.getName());
        setImageUrl(group.getPhotoUrl(), selectedGroupViewHolder.imageView);

        selectedGroupViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public void setImageUrl(String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.loading) //default pic
                .centerCrop()
                //.error(Drawable pic)  picture has problem
                //.transform(new CircleTransform(context)) //Cool !!!
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }


    public class SelectedGroupViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView tvGroupName;

        public SelectedGroupViewHolder(View itemView) {
            super(itemView);

            tvGroupName = (TextView) itemView.findViewById(R.id.tvName);
            imageView = (ImageView) itemView.findViewById(R.id.imgGroup);
        }
    }


}
