package thefinal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Neopterix on 01/09/15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index){
            case 0:
                return new UserAccountsFragement();
            case 1:
                return new VideosFragement();
            case 2:
                return new AdminSettingsFragement();
            case 3:
                return new NewUserFragement();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
