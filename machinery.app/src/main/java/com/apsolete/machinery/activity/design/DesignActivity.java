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
    private DesignContent _currentDesign;
    private DesignContent _changeGears = new ChangeGears();
    private DesignContent _gearWheels = new GearWheels();
    private DesignContent _gearWheelsExt = new GearWheelsExt();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calculation);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        int calc_type = getIntent().getExtras().getInt("calc_type");
        DesignType calcType = DesignType.values()[calc_type];
        showDesignContent(calcType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.content_design_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.mi_action_save:
                if (_currentDesign != null)
                    _currentDesign.save();
                break;
            case R.id.mi_action_clear:
                if (_currentDesign != null)
                    _currentDesign.clear();
                break;
            case R.id.mi_action_options:
                if (_currentDesign != null)
                {
                    showDesignContentSettings();
                    //_currentDesign.setOptions();
                }
                break;
            case R.id.mi_action_close:
                if (_currentDesign != null)
                    _currentDesign.close();
                break;
        }
        return super.onOptionsItemSelected(item);
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
            //.addToBackStack(null)
            .commit();
    }
    
    private void showDesignContentSettings()
    {
        if (_currentDesign == null)
            return;
            
        Fragment fragment = _currentDesign.getSettings();

        if (fragment == null)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.content_design, fragment)
            .addToBackStack(null)
            .commit();
    }
}
