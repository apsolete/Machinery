package com.apsolete.machinery.references;

import com.apsolete.machinery.*;

import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;

public class ReferencesActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_references);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_references);
        setSupportActionBar(toolbar);
        
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
    }
    
}
