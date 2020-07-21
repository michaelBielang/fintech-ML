package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.LinkedList;
import java.util.List;

public class ApacheMatrixService {

    public static List<Row> getRowList(List<ChartDataVO> chartDataVOList, List<String> factorNames,
                                       final int[] barDeltas, final List<Indicators> technicalIndicatorsList) {
        final List<Row> rowList = new LinkedList<>();
        final INDArray indArray = Dl4JMatrixService.fillMatrixWithPredictors(chartDataVOList, factorNames, barDeltas, technicalIndicatorsList);

        for (int rowIndex = 0; rowIndex < indArray.rows(); rowIndex++) {
            final Double[] doubleVec = ArrayUtils.toObject(indArray.getRow(rowIndex).toDoubleVector());
            final Row row = RowFactory.create((Object[]) doubleVec);
            rowList.add(row);
        }
        return rowList;
    }
}
