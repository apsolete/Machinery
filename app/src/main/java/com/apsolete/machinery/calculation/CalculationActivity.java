package com.apsolete.machinery.calculation;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.apsolete.machinery.R;

public class CalculationActivity extends AppCompatActivity
{
    private boolean _isSettingsOpened;

    private CalculationFragment mCalculationFragment;
    protected CalculationDatabase mDatabase;
    private MenuItem _miSave;
    private MenuItem _miClear;
    private MenuItem _miOptions;
    private MenuItem _miClose;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDatabase = CalculationDatabase.getInstance(getApplicationContext());

        setContentView(R.layout.activity_calculation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calculation);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Show content
        int calcType = getIntent().getExtras().getInt("calcType");
        showCalculationContent(calcType);
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
        if (mCalculationFragment != null)
        {
            int id = item.getItemId();
            switch (id)
            {
                case R.id.mi_action_calculate:
                    mCalculationFragment.getViewModel().calculate();
                    break;
                case R.id.mi_action_save:
                    mCalculationFragment.getViewModel().save();
                    break;
                case R.id.mi_action_clear:
                    mCalculationFragment.getViewModel().clear();
                    break;
                case R.id.mi_action_options:
                    openCalculationSettings();
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
                    if (mCalculationFragment.getViewModel().close())
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
        else if (mCalculationFragment.close())
            onNavigateUp();
    }

    public void showCalculationContent(int type)
    {
        mCalculationFragment = CalculationFragment.create(type);

        if (mCalculationFragment == null)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_design, mCalculationFragment)
                .commit();
    }

    private void openCalculationSettings()
    {
        if (mCalculationFragment == null)
            return;

        Fragment fragment = mCalculationFragment.getSettings();

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
    }
}
