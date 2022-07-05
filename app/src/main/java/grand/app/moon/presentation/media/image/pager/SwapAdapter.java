package grand.app.moon.presentation.media.image.pager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SwapAdapter extends FragmentPagerAdapter
{
    public int id;
    private ArrayList<TabModel> tabs;
    public SwapAdapter(final FragmentManager fm, ArrayList<TabModel> tabs) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.tabs = tabs;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        if(position >= 0)
            return tabs.get(position).fragment;
        return null;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position >=  0)
            return tabs.get(position).name;
        return null;
    }


    public void refresh() {

    }
}