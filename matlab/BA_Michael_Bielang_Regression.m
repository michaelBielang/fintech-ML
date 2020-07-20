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
    tr = timerange('2007-01-01' , '2007-02-28');
    dataXin = readtable('matlab/fullTable.csv','PreserveVariableNames',true);
    dataYin = readtable('matlab/futureReturns.csv','PreserveVariableNames',true);
    % tIn = X.Time(tr);
    modelTrain = fitlm([dataXin dataYin] , 'linear');
    retPredictionRegress = predict(modelTrain , dataXin);
    
    positions = zeros(size(retPredictionRegress));
    positions(retPredictionRegress > 0) = 1;
    positions(retPredictionRegress < 0) = -1;

    actualReturns = positions .* dataYin{:,1};
    inSampleRegressionReturns = cumprod(1+actualReturns);
    
    tIn = 1:length(inSampleRegressionReturns);
    f1 = figure('tag' , 'insamplefigure');
    plot(tIn , inSampleRegressionReturns);
    title('In-Sample Results');
end

