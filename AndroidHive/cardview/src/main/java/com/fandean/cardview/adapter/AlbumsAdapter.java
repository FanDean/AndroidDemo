package com.fandean.cardview.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fandean.cardview.R;
import com.fandean.cardview.model.Album;

import java.util.List;

/**
 * Created by fan on 17-5-1.
 */

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder>{
    private Context mContext;
    private List<Album> mAlbumList;

    public AlbumsAdapter(List<Album> albumList, Context context) {
        mAlbumList = albumList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album  album = mAlbumList.get(position);
        holder.mTitle.setText(album.getName());
        holder.mCount.setText(album.getNumOfSongs());

        Glide.with(mContext).load(album.getThumbnail()).into(holder.mThumbnail);

        //为overflow图标设置点击事件处理器
        holder.mOverflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.mOverflow);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    /**
     * 使用PopupMenu创建弹出式菜单
     * @param view
     */
    private void showPopupMenu(View view){
        //inflate menu
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_album,popupMenu.getMenu());
        //为弹出的Menu设置事件处理器
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            //TODO PopupMenuItemClick 事件处理
            switch (item.getItemId()){
                case R.id.action_add_favourite:

                case R.id.action_play_next:

            }
            return false;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle, mCount;
        public ImageView mThumbnail, mOverflow;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mCount = (TextView) itemView.findViewById(R.id.count);
            mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            mOverflow = (ImageView) itemView.findViewById(R.id.overflow);
        }
    }

}
