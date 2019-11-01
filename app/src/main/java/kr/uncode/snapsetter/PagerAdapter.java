package kr.uncode.snapsetter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1 :
                NewHomeFragment newHomeFragment = new NewHomeFragment();
                return newHomeFragment;

                default:
                    return null;

        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
