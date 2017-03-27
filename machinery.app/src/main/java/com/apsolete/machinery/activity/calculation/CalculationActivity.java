package com.apsolete.machinery.activity.calculation;

import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import com.apsolete.machinery.activity.*;

public class CalculationActivity  extends AppCompatActivity
{
    private CalculationContent _changeGears = new ChangeGearsCalculation();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_calculation);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        int calc_type = getIntent().getExtras().getInt("calc_type");
        CalculationType calcType = CalculationType.values()[calc_type];
        showCalculationContent(calcType);
    }
    
    public void showCalculationContent(CalculationType type)
    {
        Fragment fragment = null;
        switch (type)
        {
            case ChangeGears:
                fragment = _changeGears;
                break;
            case GearsCommon: break;
            case GearsExtended: break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
            .replace(R.id.content_calculation, fragment)
            .addToBackStack(null)
            .commit();
    }
}
