package com.fandean.cardview.activity;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fandean.cardview.R;
import com.fandean.cardview.adapter.AlbumsAdapter;
import com.fandean.cardview.model.Album;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Album> mAlbumList;
    private AlbumsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
//        toolbar.setTitleTextColor(Color.WHITE); // 设置标题文本颜色
        setSupportActionBar(toolbar);
        //在toolbar上显示icon
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Shows or hides the toolbar title when the toolbar is collapsed or expanded.
        initCollapsingToolbar();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAlbumList = new ArrayList<>();
        mAdapter = new AlbumsAdapter(mAlbumList, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        //TODO 设置Item动画和装饰
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

        //Adds sample albums data required for the recycler view. 并通知数据已经改变。
        prepareAlbums();

        try{
            //加载图片
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */

    private void initCollapsingToolbar(){
        //常量才可在匿名内部类中使用
        final CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        //TODO 关闭该标题，而启用toolbar的标题；因为暂时它显示不正确，并且没有找到原因
        collapsingToolbarLayout.setTitleEnabled(false);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        //扩展该AppbarLayout
        appBarLayout.setExpanded(true);

       // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1){
                    //获取AppBarLayout的最大滑动距离
                    scrollRange = appBarLayout.getTotalScrollRange();
                    Log.d("fandean", "1 偏移距离TotalScrollRange： " + scrollRange);
                    Log.d("fandean", "1 verticalOffset: 垂直偏移量： " + verticalOffset);
                }
                //verticalOffset: 垂直偏移量
                //已经为最大滑动距离
                if (scrollRange + verticalOffset == 0){
                    //设置标题
                    collapsingToolbarLayout.setTitle("View Card");
//                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    //显示标题
                    isShow = true;

                    Log.d("fandean", "2 偏移距离TotalScrollRange： " + scrollRange);
                    Log.d("fandean", "2 verticalOffset: 垂直偏移量： " + verticalOffset);
                }else if (isShow){
                    //如果已经显示标题
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;

                    Log.d("fandean", "3 偏移距离TotalScrollRange： " + scrollRange);
                    Log.d("fandean", "3 verticalOffset: 垂直偏移量： " + verticalOffset);
                }
            }
        });
    }
    /*
// 已经排除此函数的问题
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
*/

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums(){
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11,
        };

        Album a = new Album("True Romance", 13, covers[0]);
        mAlbumList.add(a);

        a = new Album("Xscpae", 8, covers[1]);
        mAlbumList.add(a);

        a = new Album("Maroon 5", 11, covers[2]);
        mAlbumList.add(a);

        a = new Album("Born to Die", 12, covers[3]);
        mAlbumList.add(a);

        a = new Album("Honeymoon", 14, covers[4]);
        mAlbumList.add(a);

        a = new Album("I Need a Doctor", 1, covers[5]);
        mAlbumList.add(a);

        a = new Album("Loud", 11, covers[6]);
        mAlbumList.add(a);

        a = new Album("Legend", 14, covers[7]);
        mAlbumList.add(a);

        a = new Album("Hello", 11, covers[8]);
        mAlbumList.add(a);

        a = new Album("Greatest Hits", 17, covers[9]);
        mAlbumList.add(a);

        mAdapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
