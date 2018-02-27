package com.apsolete.machinery.calculation;

import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.apsolete.machinery.*;
import com.apsolete.machinery.calculation.changegears.*;
import com.apsolete.machinery.calculation.gearwheels.*;
import com.apsolete.machinery.calculation.gearwheelsext.*;

import android.support.v7.widget.Toolbar;

public class DesignActivity extends AppCompatActivity
{
    private boolean _isSettingsOpened;
    //private DesignContent _currentDesign;
    //private DesignContent _latheChangeGears = new ChangeGearsView();
    //private DesignContent _gearWheels = new GearWheels();
    //private DesignContent _gearWheelsExt = new GearWheelsExt();
    private CalculationView _currentDesign;
    private ChangeGearsViewExt _cgView;// = new ChangeGearsViewExt();
    private CalculationPresenter _presenter;
    private MenuItem _miSave;
    private MenuItem _miClear;
    private MenuItem _miOptions;
    private MenuItem _miClose;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calculation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calculation);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Show content
        int designType = getIntent().getExtras().getInt("designType");
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (_currentDesign != null)
        {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.mi_action_calculate:
                    //_currentDesign.calculate();
                    _presenter.calculate();
                    break;
                case R.id.mi_action_save:
                    //_currentDesign.save();
                    _presenter.save();
                    break;
                case R.id.mi_action_clear:
                    //_currentDesign.clear();
                    _presenter.clear();
                    break;
                case R.id.mi_action_options:
                    openDesignContentSettings();
                    break;
                case R.id.mi_action_close:
                // button Up pressed
                case android.R.id.home:
                {
                    if (_isSettingsOpened)
                    {
                        // emulate Back press
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.popBackStack();
                        _isSettingsOpened = false;
                        setOptionsMenuEnabled(!_isSettingsOpened);
                        return true;
                    }
                    if (_presenter.close())
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
        if (_isSettingsOpened == true)
        {
            _isSettingsOpened = false;
            setOptionsMenuEnabled(!_isSettingsOpened);
            super.onBackPressed();
        }
        else if (_presenter.close())
            onNavigateUp();
    }

    public void showDesignContent(int type)
    {
        switch (type)
        {
            case DesignContent.CHANGEGEARS:
                //_currentDesign = _latheChangeGears;
                _cgView = new ChangeGearsViewExt();
                _presenter = new ChangeGearsPresenter(_cgView);
                _currentDesign = _cgView;
                break;
            case DesignContent.GEARWHEELS:
                //_currentDesign = _gearWheels;
                break;
            case DesignContent.GEARWHEELSEXT:
                //_currentDesign = _gearWheelsExt;
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

    private void openDesignContentSettings()
    {
//        if (_currentDesign == null)
//            return;
//
//        Fragment fragment = _currentDesign.getSettings();
//
//        if (fragment == null)
//            return;
//
//        _isSettingsOpened = true;
//        setOptionsMenuEnabled(!_isSettingsOpened);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//            .replace(R.id.content_design, fragment)
//            .addToBackStack(null)
//            .commit();
    }

    private void setOptionsMenuEnabled(boolean enable)
    {
        _miSave.setEnabled(enable);
        _miSave.getIcon().setAlpha(enable ? 255 : 130);
        _miClear.setEnabled(enable);
        _miClear.getIcon().setAlpha(enable ? 255 : 130);
        _miOptions.setEnabled(enable);
        _miOptions.getIcon().setAlpha(enable ? 255 : 130);
        _miClose.setEnabled(enable);
        _miClose.getIcon().setAlpha(enable ? 255 : 130);
        this.setProgress(5);
    }
}
