package com.gkpoter.voiceShare.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import com.gkpoter.voiceShare.R;
import com.gkpoter.voiceShare.listener.RefashListener;
import com.gkpoter.voiceShare.ui.Fragment.*;
import com.gkpoter.voiceShare.util.FinishListActivity;
import com.gkpoter.voiceShare.viewpagertransformer.*;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by dy on 2016/10/19.
 */
public class MainActivity extends FragmentActivity{

    private Fragment[] mFragments;
    private FragmentPagerAdapter adapter;
    private ViewPager viewPager;
    public static RefashListener refashListener;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        FinishListActivity.getInstance().addActivity(this);

        init();

        showView();
    }

    private void showView() {
        adapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragments[i];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //super.destroyItem(container, position, object);
            }
        };
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //lightMenu(i);
                switch (i){
                    case 0:bottomBar.selectTabWithId(R.id.tab_home);break;
                    case 1:bottomBar.selectTabWithId(R.id.tab_hot);break;
                    case 2:bottomBar.selectTabWithId(R.id.tab_collects);break;
                    case 3:bottomBar.selectTabWithId(R.id.tab_self);break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true,new ScaleInOutTransformer());
    }

    private void init() {
        viewPager= (ViewPager) findViewById(R.id.main_activity_viewpager);

        mFragments = new Fragment[4];
        mFragments[0]=new MainFragment();
        mFragments[1]=new TopFragment();
        mFragments[2]=new CollectsFragment();
        mFragments[3]=new SelfFragment();

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.tab_hot:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.tab_collects:
                        viewPager.setCurrentItem(2);
                        refashListener.back();
                        break;
                    case R.id.tab_self:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent= new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
