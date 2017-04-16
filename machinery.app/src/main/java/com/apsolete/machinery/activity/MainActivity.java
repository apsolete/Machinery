package com.apsolete.machinery.activity;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import com.apsolete.machinery.activity.calculation.*;
import com.apsolete.machinery.activity.fabs.*;
import com.apsolete.machinery.activity.references.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;

public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _drawerToggle;
    private FabsManager _fabs;

    private ContentMain _contentMain = new ContentMain();
    private ContentSettings _contentSettings = new ContentSettings();
    private ContentHelp _contentHelp = new ContentHelp();
    private ContentAbout _contentAbout = new ContentAbout();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _fabs = new FabsManager(this);
        _fabs.setClickListener(new FabsManager.OnFabClickListener()
        {
            public void OnClick(int fabId)
            {
                //Snackbar.make(findViewById(R.id.contentLayout), "Fab " + fabId + " was tapped.", Snackbar.LENGTH_SHORT).show();
                DesignType type;
                switch (fabId)
                {
                    case R.id.fab1:
                        type = DesignType.GearWheelsExt;
                        break;
                    case R.id.fab2:
                        type = DesignType.GearWheels;
                        break;
                    case R.id.fab3:
                    default:
                        type = DesignType.ChangeGears;
                        break;
                }
                showDesignActivity(type);
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
            case R.id.mi_change_gears:
                showDesignActivity(DesignType.ChangeGears);
                break;
            case R.id.mi_gearwheels:
                showDesignActivity(DesignType.GearWheels);
                break;
            case R.id.mi_gearwheels_extended:
                showDesignActivity(DesignType.GearWheelsExt);
                break;
            case R.id.mi_belt:
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
            .addToBackStack(null)
            .commit();
    }
    
    private void showDesignActivity(DesignType type)
    {
        Intent intent = new Intent();
        intent.setClass(this, DesignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("calc_type", type.Value);
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
