package com.apsolete.machinery.contents;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.CustomFragment;

public class AboutFragment extends CustomFragment<AboutViewModel>
{
    public AboutFragment()
    {
        super(R.layout.view_about, R.string.nav_about, AboutViewModel.class);
    }

    @Override
    public AboutViewModel getViewModel()
    {
        return null;
    }
}
