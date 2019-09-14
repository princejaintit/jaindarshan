package com.jain.temple.jainmandirdarshan.activity;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.jain.temple.jainmandirdarshan.Fragment.FavoriteAudioFragment;
import com.jain.temple.jainmandirdarshan.Fragment.FavoriteTempleFragment;
import com.jain.temple.jainmandirdarshan.Fragment.FavouriteContentFragment;
import com.jain.temple.jainmandirdarshan.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/4/2017.
 */

public class FavouriteActivity extends BaseActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CoordinatorLayout mainCoordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        initView();
    }

    private void initView() {
        mainCoordinatorLayout= (CoordinatorLayout) findViewById(R.id.mainCoordinatorLayout);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FavouriteContentFragment(), "Content");
        adapter.addFragment(new FavoriteTempleFragment(), "Temple");
        adapter.addFragment(new FavoriteAudioFragment(), "Audio");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void showsnackBarMessage(String msg) {
        final Snackbar snackbar = Snackbar.make(mainCoordinatorLayout, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }


}
