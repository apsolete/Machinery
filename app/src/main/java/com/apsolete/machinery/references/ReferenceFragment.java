package com.apsolete.machinery.references;

import com.apsolete.machinery.R;
import com.apsolete.machinery.common.CustomFragment;

public class ReferenceFragment extends CustomFragment<ReferenceViewModel>
{
    public ReferenceFragment()
    {
        super(R.layout.view_reference, R.string.title_references, ReferenceViewModel.class);
    }

    @Override
    public ReferenceViewModel getViewModel()
    {
        return null;
    }
}
