package com.apsolete.machinery.contents;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.CustomFragment;

public class StartPageFragment extends CustomFragment<StartPageViewModel>
{
    public StartPageFragment()
    {
        super(R.layout.view_start_page, R.string.title_start_page, StartPageViewModel.class);
    }

    @Override
    public StartPageViewModel getViewModel()
    {
        return null;
    }
}
