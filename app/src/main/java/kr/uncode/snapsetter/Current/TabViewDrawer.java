package kr.uncode.snapsetter.Current;

import android.app.Activity;
import android.content.Context;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.zip.Inflater;

import kr.uncode.snapsetter.MainActivity;
import kr.uncode.snapsetter.PagerAdapter;
import kr.uncode.snapsetter.R;
import kr.uncode.snapsetter.databinding.DrawerTabviewBinding;

public class TabViewDrawer extends AppCompatActivity {

    DrawerTabviewBinding binding;
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Toolbar toolbar;

    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_tabview);


        fragmentToolbarSet();
        mContext = getApplicationContext();
        mTabLayout = findViewById(R.id.tab_layout);

        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView("내보관함")));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(createTabView("최근본목록")));

        mViewPager = findViewById(R.id.pager_content);

        pagerAdapter = new PagerAdapter(
                getSupportFragmentManager(),mTabLayout.getTabCount());
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void fragmentToolbarSet() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //자동 네비게이션 토글 버튼 연결
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alldelete, menu);
        return true;
    }

    private View createTabView(String tabName) {
//        if (tabName == null) {
//            Log.d("xx" ,"널임");
//        }
        View tabView = LayoutInflater.from(mContext).inflate(R.layout.custom_tab,null);
        TextView txt_name = tabView.findViewById(R.id.txt_name);
        txt_name.setText(tabName);
        return tabView;
    }

}
