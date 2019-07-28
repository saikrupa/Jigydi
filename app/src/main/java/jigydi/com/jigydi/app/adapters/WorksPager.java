package jigydi.com.jigydi.app.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import jigydi.com.jigydi.app.fragments.AssignedWorks;
import jigydi.com.jigydi.app.fragments.AvailableWorks;

/**
 * Created by Saikrupa on 6/13/2017.
 */

public class WorksPager  extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    int possition;
    private String[] tabTitles = new String[]{"Available Works", "Assigned works"};
    boolean navToAssigned;
    Bundle bundle;
    //Constructor to the class
    public WorksPager(FragmentManager fm, int tabCount,boolean navToAssigned) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
        this.possition = possition;
        this.navToAssigned=navToAssigned;
        bundle=new Bundle();
        bundle.putBoolean("nav_type",navToAssigned);
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                AvailableWorks availableWorks = new AvailableWorks();
                availableWorks.setArguments(bundle);
                return availableWorks;
            case 1:
                AssignedWorks assignedWorks = new AssignedWorks();
                assignedWorks.setArguments(bundle);
                return assignedWorks;

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }



}