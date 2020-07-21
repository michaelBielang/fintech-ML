%% Regression part of my bachelor thesis written in Matlab
% This app runs a regression on pre-prepared data delivered by my Java Spring Application.
%
% First I make us of a Linear Regression and then of a Stepwise Regression.
%

function BA_Michael_Bielang_Regression()

    %% IN SAMPLE

    dataXin = readtable('matlab/trainingData.csv','PreserveVariableNames',true);
    dataYin = readtable('matlab/futureReturns.csv','PreserveVariableNames',true);
    % tIn = X.Time(tr);
    %% Train
    fitLmModel = fitlm([dataXin dataYin] , 'linear')

    %% Predict
    linearRegPredictor = predict(fitLmModel , dataXin);

    %% View
    positions = zeros(size(linearRegPredictor));
    positions(linearRegPredictor > 0) = 1;
    positions(linearRegPredictor < 0) = -1;

    actualReturns = positions .* dataYin{:,1};
    inSampleRegressionReturns = cumprod(1+actualReturns);

    tIn = 1:length(inSampleRegressionReturns);

    %% OUT Sample

    testDataX = readtable('matlab/testData.csv','PreserveVariableNames',true);
    testDataY = readtable('matlab/returns.csv','PreserveVariableNames',true);

    testDataPredictor = predict(fitLmModel , testDataX);

    positions=zeros(size(testDataX,1), 1);
    positions(testDataPredictor > 0)=1;
    positions(testDataPredictor < 0)=-1;

    actualReturns = positions .* testDataY{:,1};
    outSampleRegressionReturns = cumprod(1 + actualReturns);

    tOut = 1:length(outSampleRegressionReturns);

    %% Stepwise

    modelStepwise = stepwiselm([dataXin dataYin] , 'linear' , 'upper' , 'linear')

    %% Predict In-Sample results
    testDataPredictor = predict(modelStepwise , dataXin);

    positions = zeros(size(testDataPredictor));
    positions(testDataPredictor > 0) = 1;
    positions(testDataPredictor < 0) = -1;

    actualReturns = positions .* dataYin{:,1};
    inSampleStepwiseReturns = cumprod(1+actualReturns);

    %% Run for our out of sample
    testDataPredictor = predict(modelStepwise , testDataX);
    positions=zeros(size(testDataPredictor,1), 1);
    positions(testDataPredictor > 0)=1;
    positions(testDataPredictor < 0)=-1;

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

