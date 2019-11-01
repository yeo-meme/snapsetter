package kr.uncode.snapsetter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import kr.uncode.snapsetter.Drawer.DrawerFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;


    public PagerAdapter(@NonNull FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 :
                DrawerFragment drawerFragment = new DrawerFragment();
                return drawerFragment;

            case 1 :
                CurrentDrawerFragment currentDrawerFragment = new CurrentDrawerFragment();
                return currentDrawerFragment;

                default:
                    return null;

        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
