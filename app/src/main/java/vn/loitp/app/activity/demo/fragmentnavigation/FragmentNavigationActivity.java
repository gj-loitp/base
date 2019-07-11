package vn.loitp.app.activity.demo.fragmentnavigation;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.core.base.BaseFontActivity;
import com.core.utilities.LLog;
import com.google.android.material.navigation.NavigationView;

import loitp.basemaster.R;

public class FragmentNavigationActivity extends BaseFontActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final String T = "loitpp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_fragment_navigation;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        LLog.d(T, "onNavigationItemSelected " + menuItem.getTitle());
        return false;
    }

    public NavController getNavController() {
        return Navigation.findNavController(activity, R.id.my_nav_host_fragment);
    }
}
