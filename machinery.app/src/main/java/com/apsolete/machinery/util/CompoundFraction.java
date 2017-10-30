package com.apsolete.machinery.util;

import java.util.*;

public class CompoundFraction extends Fraction
    {
        protected List<Fraction> _fractions = new ArrayList<>();

        private CompoundFraction(Fraction fraction)
        {
            super(fraction.getNumerator(), fraction.getDenominator());
        }

        public CompoundFraction(List<Fraction> fractions)
        {
            this(Fractions.product(fractions));
            Collections.copy(_fractions, fractions);
        }
    }
