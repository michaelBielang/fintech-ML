%% FX regression/stepwise/machine learning backtest
%
% This demo shows how to apply some simple regression and machine learning
% techniques to intraday FX trading. In our demo, we want to attempt to
% predict the 60 minute future return of a particular FX pair using a
% number of techniques
%
% * Linear Regression
% * Stepwise Regression
% * Machine Learning (specifically a classification tree)
%
% We will test this across one of the most liquid FX pairs, namely
% eurodollar.

% Copyright 2017 The MathWorks, Inc.

%% Data
%
% The data we have is ten years worth of one-minute bar prices for a series
% of currency pairs.
% We will take EURUSD as the pair in question.


function BA_Michael_Bielang_Regression()

    %% IN SAMPLE

    dataXin = readtable('matlab/trainingData.csv','PreserveVariableNames',true);
    dataYin = readtable('matlab/futureReturns.csv','PreserveVariableNames',true);
    % tIn = X.Time(tr);
    %% Train
    modelTrain = fitlm([dataXin dataYin] , 'linear')
    
    %% Predict
    retPredictionRegress = predict(modelTrain , dataXin);
    
    %% View
    positions = zeros(size(retPredictionRegress));
    positions(retPredictionRegress > 0) = 1;
    positions(retPredictionRegress < 0) = -1;

    actualReturns = positions .* dataYin{:,1};
    inSampleRegressionReturns = cumprod(1+actualReturns);
    
    tIn = 1:length(inSampleRegressionReturns);
    
    %% OUT Sample
    
    testDataX = readtable('matlab/testData.csv','PreserveVariableNames',true);
    testDataY = readtable('matlab/returns.csv','PreserveVariableNames',true);

    retPred = predict(modelTrain , testDataX);

    positions=zeros(size(testDataX,1), 1);
    positions(retPred > 0)=1;
    positions(retPred < 0)=-1;

    actualReturns = positions .* testDataY{:,1};
    outSampleRegressionReturns = cumprod(1 + actualReturns);

    tOut = 1:length(outSampleRegressionReturns);
    
    %% Stepwise
    
    modelStepwise = stepwiselm([dataXin dataYin] , 'linear' , 'upper' , 'linear')
    
    %% Predict In-Sample results
    retPrediction = predict(modelStepwise , dataXin);

    positions = zeros(size(retPrediction));
    positions(retPrediction > 0) = 1;
    positions(retPrediction < 0) = -1;

    actualReturns = positions .* dataYin{:,1};
    inSampleStepwiseReturns = cumprod(1+actualReturns);
    
    %% Run for our out of sample
    retPred = predict(modelStepwise , testDataX);
    positions=zeros(size(retPred,1), 1);
    positions(retPred > 0)=1;
    positions(retPred < 0)=-1;

    actualReturns = positions .* testDataY{:,1};

    outSampleStepwiseReturns = cumprod(1 + actualReturns);
    
    %% PLOT

    figure;
    plot(tIn , inSampleRegressionReturns, 'b');
    hold on
    title('In-Sample Results');
   
    plot(tIn, inSampleStepwiseReturns, 'r');
    legend({'Linear Regression' , 'Stepwise'});
    hold off
    
    figure;
    plot(tOut , outSampleRegressionReturns, 'b')
    hold on
    title('Out-Sample Results');

    plot(tOut , outSampleStepwiseReturns , 'r')
    legend({'Linear Regression' , 'Stepwise'});
    hold off
end

