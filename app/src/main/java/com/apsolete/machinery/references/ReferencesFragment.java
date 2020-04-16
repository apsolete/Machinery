package com.apsolete.machinery.references;


import com.apsolete.machinery.R;
import com.apsolete.machinery.common.CustomFragment;

public class ReferencesFragment extends CustomFragment<ReferencesViewModel>
{
    public ReferencesFragment()
    {
        super(R.layout.view_references_list, R.string.title_references, ReferencesViewModel.class);
    }

    @Override
    public ReferencesViewModel getViewModel()
    {
        return null;
    }
}
