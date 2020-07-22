%% Regression part of my bachelor thesis written in Matlab
% This app runs a regression on pre-prepared data delivered by my Java Spring Application.
%
% First I make us of a Linear Regression and then of a Stepwise Regression.
%

function BA_Michael_Bielang_Regression()

    %% Train data with training data

    dataXin = readtable('matlab/trainingData.csv','PreserveVariableNames',true);
    dataYin = readtable('matlab/futureReturns.csv','PreserveVariableNames',true);
    % tIn = X.Time(tr);

    %% Train
    % I am using the inbuilt function fitlm to create a linear regression model
    fitLmModel = fitlm([dataXin dataYin] , 'linear')

    %% Predict with the fitLmModel
    linearRegPredictor = predict(fitLmModel , dataXin);

    % Analyze the prediction by collecting the virtual profits
    positions = zeros(size(linearRegPredictor));

    % if the predictor is > 0 we virtually buy
    positions(linearRegPredictor > 0) = 1;

    % if the predictor is < 0 we virtually sell
    positions(linearRegPredictor < 0) = -1;

    % aggregate profits/losses
    virtualProfits = positions .* dataYin{:,1};

    % store profit/loss to plot it later
    inSampleRegressionReturns = cumprod(1+virtualProfits);

    tIn = 1:length(inSampleRegressionReturns);

    %% Test the model with test data

    testDataX = readtable('matlab/testData.csv','PreserveVariableNames',true);
    testDataY = readtable('matlab/returns.csv','PreserveVariableNames',true);

    % I create a new predictor based on the past model and fed with the test data
    testDataPredictor = predict(fitLmModel , testDataX);

    positions=zeros(size(testDataX,1), 1);
    positions(testDataPredictor > 0)=1;
    positions(testDataPredictor < 0)=-1;

    virtualProfits = positions .* testDataY{:,1};

    % store profit/loss to plot it later
    outSampleRegressionReturns = cumprod(1 + virtualProfits);

    tOut = 1:length(outSampleRegressionReturns);

    %% Stepwise lm function with the same procedure as applied above

    % I create a stepwise model
    modelStepwise = stepwiselm([dataXin dataYin] , 'linear' , 'upper' , 'linear')

    % Predict with the stepwise model
    testDataPredictor = predict(modelStepwise , dataXin);

    positions = zeros(size(testDataPredictor));
    positions(testDataPredictor > 0) = 1;
    positions(testDataPredictor < 0) = -1;

    virtualProfits = positions .* dataYin{:,1};

    % Storing the profits here
    inSampleStepwiseReturns = cumprod(1+virtualProfits);

    % Run for our out of sample
    testDataPredictor = predict(modelStepwise , testDataX);
    positions=zeros(size(testDataPredictor,1), 1);
    positions(testDataPredictor > 0)=1;
    positions(testDataPredictor < 0)=-1;

    virtualProfits = positions .* testDataY{:,1};

    % Storing profit/loss to plot it later
    outSampleStepwiseReturns = cumprod(1 + virtualProfits);

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

