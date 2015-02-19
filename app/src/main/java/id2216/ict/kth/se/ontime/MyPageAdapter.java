package id2216.ict.kth.se.ontime;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import id2216.ict.kth.se.ontime.Fragments.FragmentCustom;
import id2216.ict.kth.se.ontime.Fragments.FragmentSearch2;
import id2216.ict.kth.se.ontime.Fragments.FragmentSearchContainer;
import id2216.ict.kth.se.ontime.Fragments.FragmentSettings;
import id2216.ict.kth.se.ontime.Fragments.FragmentTimer;

/**
 * Custom implementation of FragmentPageAdapter. Specifies tab titles and which fragments to use.
 */
public class MyPageAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments = new ArrayList<Fragment>();
    Fragment hiddenSearch;

    public MyPageAdapter(FragmentManager fm) {
        super(fm);

        fragments.add(new FragmentTimer());
        fragments.add(new FragmentSearchContainer());
        fragments.add(new FragmentCustom());
        fragments.add(new FragmentSettings());

        hiddenSearch = new FragmentSearch2();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tab;

        switch (position) {
            case 0:
                tab = "Timer";
                break;
            case 1:
                tab = "Search";
                break;
            case 2:
                tab = "Custom";
                break;
            case 3:
                tab = "Settings";
                break;
            default:
                tab = "";
                break;
        }

        return tab;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }
}