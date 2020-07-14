package com.bachelorthesis.supervised_problem_solving.configuration;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames;

import java.util.Collections;
import java.util.List;

public class RuntimeDataStorage {


    private static int matrixRowLength;

    public void findAndSetMaximumMatrixRows(List<Indicators> technicalIndicatorsList, int[] barDeltas) {
        int maximum = 0;
        for (Indicators indicator : technicalIndicatorsList) {
            int maxFromIndicator = Collections.max(FactorNames.getIndicatorDeltas(indicator));
            if (maxFromIndicator > maximum) {
                maximum = maxFromIndicator;
            }
        }
        for (int delta : barDeltas) {
            if (delta > maximum) {
                maximum = delta;
            }
        }
        RuntimeDataStorage.matrixRowLength = maximum;
    }

    public static int getMatrixRowLength() {
        return matrixRowLength;
    }

    public static void setMatrixRowLength(int matrixRowLength) {
        RuntimeDataStorage.matrixRowLength = matrixRowLength;
    }
}
