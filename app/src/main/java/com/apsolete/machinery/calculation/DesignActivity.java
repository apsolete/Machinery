package com.apsolete.machinery.calculation;

import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.view.*;

import com.apsolete.machinery.*;

import android.support.v7.widget.Toolbar;

public class DesignActivity extends AppCompatActivity
{
    private boolean _isSettingsOpened;
    //private DesignContent _view;
    //private DesignContent _latheChangeGears = new View();
    //private DesignContent _gearWheels = new GearWheels();
    //private DesignContent _gearWheelsExt = new GearWheelsExt();
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
        showCalculationContent(designType);
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
        if (_presenter != null)
        {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.mi_action_calculate:
                    _presenter.calculate();
                    break;
                case R.id.mi_action_save:
                    _presenter.save();
                    break;
                case R.id.mi_action_clear:
                    _presenter.clear();
                    break;
                case R.id.mi_action_options:
                    openCalculationContentSettings();
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
        if (_isSettingsOpened)
        {
            _isSettingsOpened = false;
            setOptionsMenuEnabled(!_isSettingsOpened);
            super.onBackPressed();
        }
        else if (_presenter.close())
            onNavigateUp();
    }

    public void showCalculationContent(int type)
    {
        Fragment fragment = null;

        switch (type)
        {
            case Calculation.CHANGEGEARS: {
                com.apsolete.machinery.calculation.gearing.changegears.View view =
                        new com.apsolete.machinery.calculation.gearing.changegears.View();
                _presenter =
                        new com.apsolete.machinery.calculation.gearing.changegears.Presenter(view);
                fragment = view;
                break;
            }
            case Calculation.GEARWHEEL: {
                com.apsolete.machinery.calculation.gearing.gearwheel.View view =
                        new com.apsolete.machinery.calculation.gearing.gearwheel.View();
                _presenter =
                        new com.apsolete.machinery.calculation.gearing.gearwheel.Presenter(view);
                fragment = view;
                break;
            }
            case Calculation.GEARDRIVE: {
                com.apsolete.machinery.calculation.gearing.geardrive.View view =
                        new com.apsolete.machinery.calculation.gearing.geardrive.View();
                _presenter =
                        new com.apsolete.machinery.calculation.gearing.geardrive.Presenter(view);
                fragment = view;
                break;
            }
            case Calculation.FBELT: {
                com.apsolete.machinery.calculation.belting.fbelt.View view =
                        new com.apsolete.machinery.calculation.belting.fbelt.View();
                _presenter =
                        new com.apsolete.machinery.calculation.belting.fbelt.Presenter(view);
                fragment = view;
                break;
            }
            case Calculation.VBELT: {
                com.apsolete.machinery.calculation.belting.vbelt.View view =
                        new com.apsolete.machinery.calculation.belting.vbelt.View();
                _presenter =
                        new com.apsolete.machinery.calculation.belting.vbelt.Presenter(view);
                fragment = view;
                break;
            }
            case Calculation.PBELT: {
                com.apsolete.machinery.calculation.belting.pbelt.View view =
                        new com.apsolete.machinery.calculation.belting.pbelt.View();
                _presenter =
                        new com.apsolete.machinery.calculation.belting.pbelt.Presenter(view);
                fragment = view;
                break;
            }
            case Calculation.TBELT: {
                com.apsolete.machinery.calculation.belting.tbelt.View view =
                        new com.apsolete.machinery.calculation.belting.tbelt.View();
                _presenter =
                        new com.apsolete.machinery.calculation.belting.tbelt.Presenter(view);
                fragment = view;
                break;
            }
            case Calculation.CHAINDRIVE: {
                com.apsolete.machinery.calculation.chaindrive.View view =
                        new com.apsolete.machinery.calculation.chaindrive.View();
                _presenter =
                        new com.apsolete.machinery.calculation.chaindrive.Presenter(view);
                fragment = view;
                break;
            }
        }

        if (fragment == null)
            return;


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.content_design, fragment)
            .commit();
    }

    private void openCalculationContentSettings()
    {
        if (_presenter == null)
            return;


        Fragment fragment = _presenter.getView().getSettings();

        if (fragment == null)
            return;

        _isSettingsOpened = true;
        setOptionsMenuEnabled(!_isSettingsOpened);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.content_design, fragment)
            .addToBackStack(null)
            .commit();
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
