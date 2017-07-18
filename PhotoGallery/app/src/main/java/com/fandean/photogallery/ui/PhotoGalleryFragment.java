package com.fandean.photogallery.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fandean.photogallery.R;
import com.fandean.photogallery.bean.GalleryItem;
import com.fandean.photogallery.service.PollService;
import com.fandean.photogallery.util.FlickrFetchr;
import com.fandean.photogallery.util.QueryPreferences;
import com.fandean.photogallery.util.ThumbnailDownloader;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;



//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.Unbinder;


/**
 * This inspection integrates CheckStyle and reports in real-time on problems
 * against the current CheckStyle profile.
 * Created by fan on 17-7-10.
 */

public class PhotoGalleryFragment extends VisibleFragment {
    private static final String TAG = "PhotoGalleryFragment";
    @BindView(R.id.fragment_photo_gallery_recycler_view)
    RecyclerView mPhotoRecyclerView;
    Unbinder unbinder;

    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    private Handler responseHandler;

    public static PhotoGalleryFragment nweInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //保留Fragment实例
        setRetainInstance(true);
        //接收菜单回调方法
        setHasOptionsMenu(true);
        //后台获取json
//        new FechItemTask().execute();
        updateItems();

        //开启后台服务
//        Intent intent = PollService.newIntent(getActivity());
//        getActivity().startService(intent);
//        PollService.setServiceAlarm(getActivity(),true);
        //更改为通过菜单开启和关闭

        responseHandler = new Handler();

        //创建并启动获取图片的线程
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
            @Override
            public void onThumbnailDownloaded(PhotoHolder target, Bitmap thumbnail) {
                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                target.bindGalleryItem(drawable);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Logger.d(TAG + "Background thread startd");
//        Log.d(TAG,"Background thread startd");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        //ButterKnife无法正常工作
        unbinder = ButterKnife.bind(this, view);

        //网格布局
        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setupAdapter();

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) { //判断fragment是否已经添加到activity
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.clearQueue();
//        mThumbnailDownloader.quit();
//        Logger.i(TAG + "Background thread destroyed");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Logger.d("QueryTextSubmit: " + query);
                //将query保存到Shared preferences
                QueryPreferences.setPrefSearchQuery(getActivity(), query);
                updateItems();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.d("QueryTextChange: " + newText);
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query, false);
            }
        });

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if (PollService.isServiceAlarmOn(getActivity())) {
            toggleItem.setTitle(R.string.stop_polling);
        } else {
            toggleItem.setTitle(R.string.start_polling);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setPrefSearchQuery(getActivity(), null);
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
                //通知重建菜单
                getActivity().invalidateOptionsMenu();
                return true; //消费事件
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateItems() {
        String query = QueryPreferences.getStoredQuery(getActivity());
        new FechItemTask(query).execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class FechItemTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        private String mQuery;

        public FechItemTask(String query) {
            mQuery = query;
        }

        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            if (mQuery == null) {
                return new FlickrFetchr().fetchRecentPhotos();
            } else {
                return new FlickrFetchr().searchPhotos(mQuery);
            }
        }

        //该方法在doInBackground方法后，并在主线程中运行
        @Override
        protected void onPostExecute(List<GalleryItem> list) {
//            super.onPostExecute(list);
            mItems = list;
            setupAdapter();
        }
    }


    private class PhotoHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.fragment_photo_gallery_image_view)
        ImageView mImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            //不能在私有类中使用ButterKnife
//            ButterKnife.bind(this, itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindGalleryItem(Drawable drawable) {
            mImageView.setImageDrawable(drawable);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> list) {
            mGalleryItems = list;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, parent, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = mGalleryItems.get(position);
            //处理title中无用的信息
            String title1 = handlerTitle(item);
            Drawable drawable = getResources().getDrawable(R.drawable.md);
            holder.bindGalleryItem(drawable);
            //放入图片
            mThumbnailDownloader.queueThumbnail(holder, item.getUrl());
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }


    private String handlerTitle(GalleryItem item) {
        String title = item.getCaption();
        if (title.contains("#")) {
            int i = title.indexOf('#');
            title = title.substring(0, i);
//                Logger.d("去除#号：" + title);
        }
        if (title.contains(":")) {
            title = title.substring(0, title.indexOf(':'));
//                Logger.d("去除:号：" + title);
        }
        if (title.contains("http")) {
            title = title.substring(0, title.indexOf("http"));
//                Logger.d("去除http ：" + title);
        }
        return title;
    }
}
