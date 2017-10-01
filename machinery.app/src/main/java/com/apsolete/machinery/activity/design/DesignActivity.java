package com.apsolete.machinery.activity.design;

import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import com.apsolete.machinery.activity.*;
import com.apsolete.machinery.activity.design.changegears.*;
import com.apsolete.machinery.activity.design.gearwheels.*;
import com.apsolete.machinery.activity.design.gearwheelsext.*;

public class DesignActivity  extends AppCompatActivity
{
    private boolean _isOptionsOpened;
    private DesignContent _currentDesign;
    private DesignContent _changeGears = new ChangeGears();
    private DesignContent _gearWheels = new GearWheels();
    private DesignContent _gearWheelsExt = new GearWheelsExt();
    private MenuItem _miSave;
    private MenuItem _miClear;
    private MenuItem _miOptions;
    private MenuItem _miClose;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calculation);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Show content
        int intDesignType = getIntent().getExtras().getInt("designType");
        DesignType designType = DesignType.values()[intDesignType];
        showDesignContent(designType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.content_design_menu, menu);
        
        _miSave = menu.findItem(R.id.mi_action_save);
        _miClear = menu.findItem(R.id.mi_action_clear);
        _miOptions = menu.findItem(R.id.mi_action_options);
        _miClose = menu.findItem(R.id.mi_action_close);
        
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        _miSave.setEnabled(!_isOptionsOpened);
        _miSave.getIcon().setAlpha(_isOptionsOpened ? 130 : 255);
        _miClear.setEnabled(!_isOptionsOpened);
        _miClear.getIcon().setAlpha(_isOptionsOpened ? 130 : 255);
        _miOptions.setEnabled(!_isOptionsOpened);
        _miOptions.getIcon().setAlpha(_isOptionsOpened ? 130 : 255);
        _miClose.setEnabled(!_isOptionsOpened);
        _miClose.getIcon().setAlpha(_isOptionsOpened ? 130 : 255);
        
        return super.onPrepareOptionsMenu(menu);
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (_currentDesign != null)
        {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.mi_action_save:
                    _currentDesign.save();
                    break;
                case R.id.mi_action_clear:
                    _currentDesign.clear();
                    break;
                case R.id.mi_action_options:
                    {
                        showDesignContentSettings();
                        //_currentDesign.setOptions();
                    }
                    break;
                case R.id.mi_action_close:
                // button Up pressed
                case android.R.id.home:
                {
                    if (_isOptionsOpened)
                    {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack();
                        _isOptionsOpened = false;
                        invalidateOptionsMenu();
                        return true;
                    }
                    if (_currentDesign.close())
                    {
                        if (id == R.id.mi_action_close)
                            onNavigateUp();
                    }
                    else
                        return false;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (_isOptionsOpened == true)
        {
            _isOptionsOpened = false;
            invalidateOptionsMenu();
            super.onBackPressed();
        }
        else if (_currentDesign.close())
            onNavigateUp();
    }

    public void showDesignContent(DesignType type)
    {
        switch (type)
        {
            case ChangeGears:
                _currentDesign = _changeGears;
                break;
            case GearWheels:
                _currentDesign = _gearWheels;
                break;
            case GearWheelsExt:
                _currentDesign = _gearWheelsExt;
                break;
        }

        if (_currentDesign == null)
            return;

        Fragment fragment = _currentDesign;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.content_design, fragment)
            .commit();
    }

    private void showDesignContentSettings()
    {
        if (_currentDesign == null)
            return;

        Fragment fragment = _currentDesign.getSettings();

        if (fragment == null)
            return;

        _isOptionsOpened = true;
        invalidateOptionsMenu();
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.content_design, fragment)
            .addToBackStack(null)
            .commit();
    }
}
