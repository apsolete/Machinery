package com.apsolete.machinery.contents;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.CustomFragment;

public class HelpFragment extends CustomFragment<HelpViewModel>
{
    public HelpFragment()
    {
        super(R.layout.view_help, R.string.nav_help, HelpViewModel.class);
    }

    @Override
    public HelpViewModel getViewModel()
    {
        return null;
    }
}
