package com.apsolete.machinery.calculation.gearing.changegears;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apsolete.machinery.R;
import com.apsolete.machinery.calculation.CalculationFragment;
import com.apsolete.machinery.common.DialogBase;
import com.apsolete.machinery.common.G;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import java.util.List;

public class ChangeGearsFragment extends CalculationFragment<ChangeGearsViewModel>
{
    private ViewGroup _resultView;

    private View.OnClickListener mGearSetClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int set = -1;
            switch (v.getId())
            {
                case R.id.z0Set: set = G.Z0; break;
                case R.id.z1Set: set = G.Z1; break;
                case R.id.z2Set: set = G.Z2; break;
                case R.id.z3Set: set = G.Z3; break;
                case R.id.z4Set: set = G.Z4; break;
                case R.id.z5Set: set = G.Z5; break;
                case R.id.z6Set: set = G.Z6; break;
            }

            if (set >= 0)
                requestGearSet(set);
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

        _resultView = view.findViewById(R.id.resultsLayout);

        view.findViewById(R.id.z0Set).setOnClickListener(mGearSetClickListener);
        view.findViewById(R.id.z1Set).setOnClickListener(mGearSetClickListener);
        view.findViewById(R.id.z2Set).setOnClickListener(mGearSetClickListener);
        view.findViewById(R.id.z3Set).setOnClickListener(mGearSetClickListener);
        view.findViewById(R.id.z4Set).setOnClickListener(mGearSetClickListener);
        view.findViewById(R.id.z5Set).setOnClickListener(mGearSetClickListener);
        view.findViewById(R.id.z6Set).setOnClickListener(mGearSetClickListener);

        setCheckableObserver(R.id.oneSetOfGears, mViewModel.getOneSet());

        setCheckableObserver(R.id.z1Switch, mViewModel.gearSet(G.Z1).isSwitched());
        setCheckableObserver(R.id.z2Switch, mViewModel.gearSet(G.Z2).isSwitched());
        setCheckableObserver(R.id.z3Switch, mViewModel.gearSet(G.Z3).isSwitched());
        setCheckableObserver(R.id.z4Switch, mViewModel.gearSet(G.Z4).isSwitched());
        setCheckableObserver(R.id.z5Switch, mViewModel.gearSet(G.Z5).isSwitched());
        setCheckableObserver(R.id.z6Switch, mViewModel.gearSet(G.Z6).isSwitched());

        setVisibilityObserver(R.id.z0Gears, mViewModel.getOneSet());
        setVisibilityInverseObserver(R.id.z1Gears, mViewModel.getOneSet());
        setVisibilityInverseObserver(R.id.z2Gears, mViewModel.getOneSet());
        setVisibilityInverseObserver(R.id.z3Gears, mViewModel.getOneSet());
        setVisibilityInverseObserver(R.id.z4Gears, mViewModel.getOneSet());
        setVisibilityInverseObserver(R.id.z5Gears, mViewModel.getOneSet());
        setVisibilityInverseObserver(R.id.z6Gears, mViewModel.getOneSet());

        setEnableObserver(new int[]{R.id.z0Set, R.id.z0Gears}, mViewModel.gearSet(G.Z0).isEditAllowed());
        setEnableObserver(new int[]{R.id.z1Set, R.id.z1Gears}, mViewModel.gearSet(G.Z1).isEditAllowed());
        setEnableObserver(new int[]{R.id.z2Set, R.id.z2Gears}, mViewModel.gearSet(G.Z2).isEditAllowed());
        setEnableObserver(new int[]{R.id.z3Set, R.id.z3Gears}, mViewModel.gearSet(G.Z3).isEditAllowed());
        setEnableObserver(new int[]{R.id.z4Set, R.id.z4Gears}, mViewModel.gearSet(G.Z4).isEditAllowed());
        setEnableObserver(new int[]{R.id.z5Set, R.id.z5Gears}, mViewModel.gearSet(G.Z5).isEditAllowed());
        setEnableObserver(new int[]{R.id.z6Set, R.id.z6Gears}, mViewModel.gearSet(G.Z6).isEditAllowed());

        setEnableObserver(R.id.z1Switch, mViewModel.gearSet(G.Z1).isEnabled());
        setEnableObserver(R.id.z2Switch, mViewModel.gearSet(G.Z2).isEnabled());
        setEnableObserver(R.id.z3Switch, mViewModel.gearSet(G.Z3).isEnabled());
        setEnableObserver(R.id.z4Switch, mViewModel.gearSet(G.Z4).isEnabled());
        setEnableObserver(R.id.z5Switch, mViewModel.gearSet(G.Z5).isEnabled());
        setEnableObserver(R.id.z6Switch, mViewModel.gearSet(G.Z6).isEnabled());

        setEditTextObserver(R.id.z0Gears, mViewModel.gearSet(G.Z0).getGearsStr());
        setEditTextObserver(R.id.z1Gears, mViewModel.gearSet(G.Z1).getGearsStr());
        setEditTextObserver(R.id.z2Gears, mViewModel.gearSet(G.Z2).getGearsStr());
        setEditTextObserver(R.id.z3Gears, mViewModel.gearSet(G.Z3).getGearsStr());
        setEditTextObserver(R.id.z4Gears, mViewModel.gearSet(G.Z4).getGearsStr());
        setEditTextObserver(R.id.z5Gears, mViewModel.gearSet(G.Z5).getGearsStr());
        setEditTextObserver(R.id.z6Gears, mViewModel.gearSet(G.Z6).getGearsStr());

        setSpinnerObserver(R.id.calcTypeSpinner, R.array.cg_calctype_array, mViewModel.getCalculationMode());
        setSpinnerEnumObserver(R.id.threadPitchUnit, R.array.cg_pitchunit_array, mViewModel.getThreadPitchUnit(), ThreadPitchUnit.values());
        setSpinnerEnumObserver(R.id.leadScrewPitchUnit, R.array.cg_pitchunit_array, mViewModel.getLeadscrewPitchUnit(), ThreadPitchUnit.values());

        setVisibilityObserver(new int[]{R.id.threadPitchText, R.id.threadPitchValue, R.id.threadPitchUnit},
                mViewModel.getThreadPitchEnabled());
        setVisibilityObserver(new int[]{R.id.leadScrewPitchText, R.id.leadScrewPitchValue, R.id.leadScrewPitchUnit},
                mViewModel.getLeadscrewPitchEnabled());

        setEditTextDoubleObserver(R.id.threadPitchValue, mViewModel.getThreadPitch());
        setEditTextDoubleObserver(R.id.leadScrewPitchValue, mViewModel.getLeadscrewPitch());

        setEditTextDoubleObserver(R.id.ratioValue, mViewModel.getRatio());
        setEditTextIntegerObserver(R.id.ratioNumeratorValue, mViewModel.getRatioNumerator());
        setEditTextIntegerObserver(R.id.ratioDenominatorValue, mViewModel.getRatioDenominator());
        setCheckableObserver(R.id.ratioFractionSwitch, mViewModel.getRatioAsFraction());
        setVisibilityObserver(new int[]{R.id.ratioTextLayout, R.id.ratioLayout}, mViewModel.getRatioEnabled());
        setVisibilityInverseObserver(R.id.ratioValueLayout, mViewModel.getRatioAsFraction());
        setVisibilityObserver(R.id.ratioFractionLayout, mViewModel.getRatioAsFraction());
        setVisibilityObserver(R.id.ratioCalculated, mViewModel.getRatioCalculatedEnabled());
        setTextObserver(R.id.ratioCalculated, mViewModel.getRatioCalculated());

        setTextObserver(R.id.resultFirstNumberText, mViewModel.getFirstResultNumberStr());
        setTextObserver(R.id.resultLastNumberText, mViewModel.getLastResultNumberStr());

        mViewModel.getResultsToShow().observe(getViewLifecycleOwner(), new Observer<List<ChangeGears.Result>>()
        {
            @Override
            public void onChanged(List<ChangeGears.Result> results)
            {
                _resultView.removeAllViews();
                for (ChangeGears.Result result : results)
                {
                    setResultItem(result);
                }
            }
        });
        view.findViewById(R.id.showNext).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mViewModel.getNextResults();
            }
        });
        view.findViewById(R.id.showPrev).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mViewModel.getPrevResults();
            }
        });

        return view;
    }

    private void requestGearSet(final int set)
    {
        FragmentManager fragmentManager = Activity.getSupportFragmentManager();
        final GearPickerDialog dialog = new GearPickerDialog();
        dialog.setGears(mViewModel.gearSet(set).getGearsStr().getValue());
        dialog.setResultListener(new DialogBase.ResultListener()
        {
            @Override
            public void onPositive()
            {
                Integer[] gears = dialog.getGears();
                mViewModel.setGearSet(set, gears);
            }

            @Override
            public void onNegative()
            {}
        });
        dialog.show(fragmentManager, "teethnumbersdialog");
    }

    private void setResultItem(final ChangeGears.Result result)
    {
        try
        {
            LayoutInflater layoutInflater = (LayoutInflater)Activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            android.view.View view = layoutInflater.inflate(R.layout.view_changegears_result2, null);

            TextView text = (TextView)view.findViewById(R.id.resultNumberText);
            text.setText(result.id());

            int visibility;
            text = (TextView)view.findViewById(R.id.z1Text);
            text.setText(result.z1());
            text = (TextView)view.findViewById(R.id.z2Text);
            text.setText(result.z2());

            visibility = result.z3() != null ? android.view.View.VISIBLE : android.view.View.GONE;
            view.findViewById(R.id.gears34Layout).setVisibility(visibility);
            if (visibility == android.view.View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.z3Text);
                text.setText(result.z3());
                text = (TextView)view.findViewById(R.id.z4Text);
                text.setText(result.z4());
            }

            visibility = result.z5() != null ? android.view.View.VISIBLE : android.view.View.GONE;
            view.findViewById(R.id.gears56Layout).setVisibility(visibility);
            if (visibility == android.view.View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.z5Text);
                text.setText(result.z5());
                text = (TextView)view.findViewById(R.id.z6Text);
                text.setText(result.z6());
            }

            text = (TextView)view.findViewById(R.id.ratioText);
            text.setText(result.ratio());

            visibility = result.threadPitch() != null ? android.view.View.VISIBLE : android.view.View.GONE;
            view.findViewById(R.id.threadPitchLayout).setVisibility(visibility);
            if (visibility == android.view.View.VISIBLE)
            {
                text = (TextView)view.findViewById(R.id.threadPitchText);
                text.setText(result.threadPitch());
            }

            _resultView.addView(view);
        }
        catch (Exception e)
        {
            //
        }
    }

}
