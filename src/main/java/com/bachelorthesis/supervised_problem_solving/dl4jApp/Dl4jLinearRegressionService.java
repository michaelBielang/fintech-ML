package com.bachelorthesis.supervised_problem_solving.dl4jApp;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.Algorithms;
import com.bachelorthesis.supervised_problem_solving.services.algos.MatrixService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import lombok.Data;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.custom.LinearSolve;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;

import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getFactorNames;
import static org.deeplearning4j.nn.weights.WeightInit.XAVIER;

@Data
public class Dl4jLinearRegressionService {

    private final RuntimeDataStorage runtimeDatastorage = new RuntimeDataStorage();

    private final int tradingFrequency = 60;

/*
    // delta between bars
    private final int[] barDelta = new int[]{5, 10, 15, 20, 25, 30, 60};
*/

    // delta between bars
    private final int[] barDelta = new int[]{5, 10, 15, 20, 25, 30, 60};

    /**
     * price - timetable of prices. I MAKE THE ASSUMPTION THAT THIS VARIABLE
     * IS OF ONE MINUTE BAR
     * predictor
     * otherSignals - function handles referring to other sigals
     *
     * @param chartDataVOList
     * @param technicalIndicatorsList function handles referring to other signals
     */
    public void calculateSignals(final List<ChartDataVO> chartDataVOList, final List<Indicators> technicalIndicatorsList) {

        runtimeDatastorage.findAndSetMaximumMatrixRows(chartDataVOList, technicalIndicatorsList, barDelta, tradingFrequency);
        validateNumberOfDataPoints(chartDataVOList);

        final List<String> factorNames = getFactorNames(technicalIndicatorsList, barDelta);

        // fill matrix with predictors

        // xInSample
        final INDArray factorMatrix = MatrixService.fillMatrixWithPredictors(chartDataVOList, factorNames, barDelta, technicalIndicatorsList);

        // y in Sample
        final List<Double> futureReturns = Algorithms.getReturns(chartDataVOList, tradingFrequency);
        INDArray indArray = Nd4j.create(futureReturns);
        // train linear regression model --> modelTrain = fitlm([XInSample yInSample] , 'linear')

        LinearSolve linearSolve = new LinearSolve(factorMatrix, indArray);
        System.out.println(linearSolve.getInputArgument(0).rows());
        //train(factorMatrix, futureReturns);

        System.out.println(factorNames);
        System.out.println(factorMatrix);
    }

    private void validateNumberOfDataPoints(List<ChartDataVO> chartDataVOList) {
        if (chartDataVOList.size() < RuntimeDataStorage.getMatrixRowLength()) {
            throw new IllegalArgumentException("Not enough data. Please deliver more");
        }
    }

    private void train(INDArray factorMatrix, List<Double> futureReturns) {
        int seed = 123;
        double learningRate = 0.01;
        int batchSize = 50;
        int nEpochs = 30;

        int numInputs = factorMatrix.columns();
        int numOutputs = 1;
        int numHiddenNodes = 20;

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .weightInit(XAVIER)
                .updater(new Nesterovs(learningRate, 0.9))
                .list()
                .layer(new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
                        .activation(Activation.RELU)
                        .build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        model.setListeners(new ScoreIterationListener(10));  //Print score every 10 parameter updates

        model.fit(factorMatrix, new int[]{numInputs});

        System.out.println("Evaluate model....");
        Evaluation eval = new Evaluation(numOutputs);
/*        while (testIter.hasNext()) {
            DataSet t = testIter.next();
            INDArray features = t.getFeatures();
            INDArray labels = t.getLabels();
            INDArray predicted = model.output(features, false);
            eval.eval(labels, predicted);
        }
        //An alternate way to do the above loop
        //Evaluation evalResults = model.evaluate(testIter);

        //Print the evaluation statistics
        System.out.println(eval.stats());

        System.out.println("\n****************Example finished********************");
        //Training is complete. Code that follows is for plotting the data & predictions only
        generateVisuals(model, trainIter, testIter);*/
    }

    // TODO: 16.07.2020 create linear regression model
    // train model
}
