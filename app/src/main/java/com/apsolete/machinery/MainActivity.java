package com.apsolete.machinery;

import com.apsolete.machinery.common.G;
import com.apsolete.machinery.contents.StartPageFragment;
import com.apsolete.machinery.contents.SettingsFragment;
import com.apsolete.machinery.contents.HelpFragment;
import com.apsolete.machinery.contents.AboutFragment;
import com.apsolete.machinery.calculation.CalculationActivity;
import com.apsolete.machinery.common.fabs.FabsView;
import com.apsolete.machinery.references.ReferencesActivity;

import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _drawerToggle;
    private FabsView _fabs;

    private StartPageFragment _contentMain = new StartPageFragment();
    private SettingsFragment _contentSettings = new SettingsFragment();
    private HelpFragment _contentHelp = new HelpFragment();
    private AboutFragment _contentAbout = new AboutFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _fabs = (FabsView)findViewById(R.id.fabsView);
        _fabs.setClickListener(new FabsView.OnFabClickListener()
        {
            public void onClick(int fabId)
            {
                int type = 0;
                switch (fabId)
                {
                    case R.id.fab_changegears:
                        type = G.CHANGEGEARS;
                        break;
                    case R.id.fab_gearwheel:
                        type = G.GEARWHEEL;
                        break;
                    case R.id.fab_geardrive:
                        type = G.GEARDRIVE;
                        break;
                    case R.id.fab_fbelt:
                        type = G.FBELT;
                        break;
                    case R.id.fab_vbelt:
                        type = G.VBELT;
                        break;
                    case R.id.fab_pbelt:
                        type = G.PBELT;
                        break;
                    case R.id.fab_tbelt:
                        type = G.TBELT;
                        break;
                    case R.id.fab_chaindrive:
                        type = G.CHAINDRIVE;
                        break;
                }
                if (type > 0)
                    showCalculationActivity(type);
            }
        });

        _drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawerToggle = new ActionBarDrawerToggle(this, _drawer, toolbar,
                                                  R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            public void onDrawerClosed(View view)
            {
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView)
            {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        _drawer.addDrawerListener(_drawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        _drawerToggle.syncState();
        showStartPage();
    }

    @Override
    public void onBackPressed()
    {
        if (_drawer.isDrawerOpen(GravityCompat.START))
        {
            _drawer.closeDrawer(GravityCompat.START);
        }
        else if(_fabs.isExpanded())
        {
            _fabs.collapse();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // TODO: Implement this method
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.mi_changegears:
                showCalculationActivity(G.CHANGEGEARS);
                break;
            case R.id.mi_gearwheel:
                showCalculationActivity(G.GEARWHEEL);
                break;
            case R.id.mi_geardrive:
                showCalculationActivity(G.GEARDRIVE);
                break;
            case R.id.mi_fbelt:
                showCalculationActivity(G.FBELT);
                break;
            case R.id.mi_vbelt:
                showCalculationActivity(G.VBELT);
                break;
            case R.id.mi_pbelt:
                showCalculationActivity(G.PBELT);
                break;
            case R.id.mi_tbelt:
                showCalculationActivity(G.TBELT);
                break;
            case R.id.mi_chaindrive:
                showCalculationActivity(G.CHAINDRIVE);
                break;
            case R.id.mi_action_save:
                break;
            case R.id.mi_action_clear:
                break;
            case R.id.mi_action_close:
                break;
            case R.id.mi_action_options:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        _drawer.closeDrawer(GravityCompat.START);

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.mi_start_page:
                showStartPage();
                break;
            case R.id.mi_references:
                showReferencesPage();
                break;
            case R.id.mi_settings:
                showSettingsPage();
                break;
            case R.id.mi_help:
                showHelpPage();
                break;
            case R.id.mi_about:
                showAboutPage();
                break;
            case R.id.mi_exit:
                exitApp();
                break;
        }

        return true;
    }

    private void showContentFragment(Fragment fragment)
    {
        _fabs.hide();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.contentLayout, fragment)
            .addToBackStack(null)
            .commit();
    }

    private void showStartPage()
    {
        _fabs.show();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.contentLayout, _contentMain)
            //.addToBackStack(null)
            .commit();
    }
    
    private void showCalculationActivity(int type)
    {
        Intent intent = new Intent();
        intent.setClass(this, CalculationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("calcType", type);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showReferencesPage()
    {
        Intent intent = new Intent();
        intent.setClass(this, ReferencesActivity.class);
        startActivity(intent);
    }

    private void showSettingsPage()
    {
        showContentFragment(_contentSettings);
    }

    private void showHelpPage()
    {
        showContentFragment(_contentHelp);
    }

    private void showAboutPage()
    {
        showContentFragment(_contentAbout);
    }

    private void exitApp()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
            .setMessage("Click yes to exit!")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    // 1st way
                    //moveTaskToBack(true);
                    //android.os.Process.killProcess(android.os.Process.myPid());
                    //System.exit(1);

                    // 2nd way
                    //Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    //homeIntent.addCategory( Intent.CATEGORY_HOME );
                    //homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
                    //startActivity(homeIntent);

                    // 3rd way
                    finish();
                    System.exit(0);
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
