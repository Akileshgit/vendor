package Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import Fragment.Fruit_fragment;
import Fragment.Vegetables_fragment;
import Fragment.Leaf_fragment;

import java.util.List;

import Config.BaseURL;
import Model.Category_model;

public class PagerHome_adapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public PagerHome_adapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fruit_fragment tab1 = new Fruit_fragment();
                return tab1;
            case 1:
                Vegetables_fragment tab2 = new Vegetables_fragment();
                return tab2;
            case 2:
                Leaf_fragment tab3 = new Leaf_fragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


