package com.apsolete.machinery.calculation.gearing.changegears;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.common.DialogBase;
import com.apsolete.machinery.common.G;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

public class ChangeGearsFragment extends CalculationFragment<ChangeGearsViewModel>
{
    private final GearKitView[] mGkViews = new GearKitView[7];

    private GearKitView.OnGearKitViewListener mGearKitViewListener = new GearKitView.OnGearKitViewListener()
    {
        @Override
        public void onRequest(GearKitView gearKit)
        {
            requestGearKit(gearKit);
        }

        @Override
        public void onChanged(GearKitView gearKit)
        {
            mViewModel.setGearKit(gearKit.getId(), gearKit.getGears());
        }

        @Override
        public void onChecked(GearKitView gearKit)
        {
            //mViewModel.setGearKitChecked(gearKit.getId(), gearKit.isChecked());
        }
    };

    public ChangeGearsFragment()
    {
        super(G.CHANGEGEARS, R.layout.view_changegears, R.string.title_changegears, ChangeGearsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        assert view != null;

        mGkViews[G.Z0] = new GearKitView(G.Z0, view, R.id.z0Kit, R.id.z0Gears, 0, mGearKitViewListener);
        mGkViews[G.Z1] = new GearKitView(G.Z1, view, R.id.z1Kit, R.id.z1Gears, R.id.z1Select, mGearKitViewListener);
        mGkViews[G.Z2] = new GearKitView(G.Z2, view, R.id.z2Kit, R.id.z2Gears, R.id.z2Select, mGearKitViewListener);
        mGkViews[G.Z3] = new GearKitView(G.Z3, view, R.id.z3Kit, R.id.z3Gears, R.id.z3Select, mGearKitViewListener);
        mGkViews[G.Z4] = new GearKitView(G.Z4, view, R.id.z4Kit, R.id.z4Gears, R.id.z4Select, mGearKitViewListener);
        mGkViews[G.Z5] = new GearKitView(G.Z5, view, R.id.z5Kit, R.id.z5Gears, R.id.z5Select, mGearKitViewListener);
        mGkViews[G.Z6] = new GearKitView(G.Z6, view, R.id.z6Kit, R.id.z6Gears, R.id.z6Select, mGearKitViewListener);

        setCheckableObserver(R.id.oneSetOfGears, mViewModel.getOneSet());

        setCheckableObserver(R.id.z1Select, mViewModel.gearSet(G.Z1).isSelected());
        setCheckableObserver(R.id.z2Select, mViewModel.gearSet(G.Z2).isSelected());
        setCheckableObserver(R.id.z3Select, mViewModel.gearSet(G.Z3).isSelected());
        setCheckableObserver(R.id.z4Select, mViewModel.gearSet(G.Z4).isSelected());
        setCheckableObserver(R.id.z5Select, mViewModel.gearSet(G.Z5).isSelected());
        setCheckableObserver(R.id.z6Select, mViewModel.gearSet(G.Z6).isSelected());

        setVisibilityObserver(R.id.z0Gears, mViewModel.getOneSet());
        setVisibilityInversedObserver(R.id.z1Gears, mViewModel.getOneSet());
        setVisibilityInversedObserver(R.id.z2Gears, mViewModel.getOneSet());
        setVisibilityInversedObserver(R.id.z3Gears, mViewModel.getOneSet());
        setVisibilityInversedObserver(R.id.z4Gears, mViewModel.getOneSet());
        setVisibilityInversedObserver(R.id.z5Gears, mViewModel.getOneSet());
        setVisibilityInversedObserver(R.id.z6Gears, mViewModel.getOneSet());

        setEnableObserver(new int[]{R.id.z0Kit, R.id.z0Gears}, mViewModel.gearSet(G.Z0).isEditAllowed());
        setEnableObserver(new int[]{R.id.z1Kit, R.id.z1Gears}, mViewModel.gearSet(G.Z1).isEditAllowed());
        setEnableObserver(new int[]{R.id.z2Kit, R.id.z2Gears}, mViewModel.gearSet(G.Z2).isEditAllowed());
        setEnableObserver(new int[]{R.id.z3Kit, R.id.z3Gears}, mViewModel.gearSet(G.Z3).isEditAllowed());
        setEnableObserver(new int[]{R.id.z4Kit, R.id.z4Gears}, mViewModel.gearSet(G.Z4).isEditAllowed());
        setEnableObserver(new int[]{R.id.z5Kit, R.id.z5Gears}, mViewModel.gearSet(G.Z5).isEditAllowed());
        setEnableObserver(new int[]{R.id.z6Kit, R.id.z6Gears}, mViewModel.gearSet(G.Z6).isEditAllowed());

        setEnableObserver(R.id.z1Select, mViewModel.gearSet(G.Z1).isEnabled());
        setEnableObserver(R.id.z2Select, mViewModel.gearSet(G.Z2).isEnabled());
        setEnableObserver(R.id.z3Select, mViewModel.gearSet(G.Z3).isEnabled());
        setEnableObserver(R.id.z4Select, mViewModel.gearSet(G.Z4).isEnabled());
        setEnableObserver(R.id.z5Select, mViewModel.gearSet(G.Z5).isEnabled());
        setEnableObserver(R.id.z6Select, mViewModel.gearSet(G.Z6).isEnabled());

        setSpinnerObserver(R.id.calcTypeSpinner, R.array.cg_calctype_array, mViewModel.getCalculationMode());
        setSpinnerEnumObserver(R.id.threadPitchUnit, R.array.cg_pitchunit_array, mViewModel.getThreadPitchUnit(), ThreadPitchUnit.values());
        setSpinnerEnumObserver(R.id.leadScrewPitchUnit, R.array.cg_pitchunit_array, mViewModel.getLeadscrewPitchUnit(), ThreadPitchUnit.values());

        setVisibilityObserver(new int[]{R.id.threadPitchText, R.id.threadPitchValue, R.id.threadPitchUnit},
                mViewModel.getThreadPitchEnabled());
        setVisibilityObserver(new int[]{R.id.leadScrewPitchText, R.id.leadScrewPitchValue, R.id.leadScrewPitchUnit},
                mViewModel.getLeadscrewPitchEnabled());

        return view;
    }

    private void requestGearKit(GearKitView gsView)
    {
        final GearKitView gsv = gsView;

        FragmentManager fragmentManager = Activity.getSupportFragmentManager();
        final TeethNumbersDialog dialog = new TeethNumbersDialog();
        dialog.setSelection(gsv.getGears());
        dialog.setResultListener(new DialogBase.ResultListener()
        {
            @Override
            public void onPositive()
            {
                Integer[] gears = dialog.getGears();
                mViewModel.setGearKit(gsv.getId(), gears);
            }

            @Override
            public void onNegative()
            {}
        });
        dialog.show(fragmentManager, "teethnumbersdialog");
    }

}
