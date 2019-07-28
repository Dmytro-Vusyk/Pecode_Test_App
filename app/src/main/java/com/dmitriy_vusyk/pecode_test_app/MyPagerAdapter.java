package com.dmitriy_vusyk.pecode_test_app;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<MyFragment> fragmentList;
    private FragmentManager fm;

    public MyPagerAdapter(FragmentManager fm, ArrayList<MyFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        Log.d("GET ITEM", String.valueOf(i));
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        MyFragment instantiatedFragment = (MyFragment) super.instantiateItem(container, position);
        Log.d("INSTANTIATE ITEM_1", String.valueOf(position));
        return instantiatedFragment;
    }

    public MyFragment getRegisteredFragment(int position) {
        if (fragmentList.size() > 0) {
            return fragmentList.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        int index = ((MyFragment) object).getFragmentId();
        Log.d("GET ITEM POSITION", String.valueOf(index));
        if (index == -1) {
            return POSITION_NONE;
        }
        return index;
    }

    public ArrayList<MyFragment> getFragmentList() {
        return fragmentList;
    }

    public void removeFragment(int position) {
        fragmentList.remove(position);
        notifyDataSetChanged();
    }
}

