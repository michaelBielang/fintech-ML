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

dataTable = readtable('fullTable.csv','PreserveVariableNames',true);
