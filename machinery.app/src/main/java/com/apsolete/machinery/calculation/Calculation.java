package com.apsolete.machinery.calculation;
import com.apsolete.machinery.common.mvp.*;

public final class Calculation
{
    public interface Contract
    {
        public interface Presenter extends BasePresenter
        {
            void save();
            void clear();
            void calculate();
        }

        public interface View extends BaseView<Presenter>
        {
        }
    }
}
