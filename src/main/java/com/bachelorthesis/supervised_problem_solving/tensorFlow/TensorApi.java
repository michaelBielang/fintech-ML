package com.bachelorthesis.supervised_problem_solving.tensorFlow;

import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.Tensor;

public class TensorApi {

    private void setup() {
        Graph graph = new Graph();

        Operation a = graph.opBuilder("Const", "a")
                .setAttr("dtype", DataType.fromClass(Double.class))
                .setAttr("value", Tensor.<Double>create(3.0, Double.class))
                .build();
        Operation b = graph.opBuilder("Const", "b")
                .setAttr("dtype", DataType.fromClass(Double.class))
                .setAttr("value", Tensor.<Double>create(2.0, Double.class))
                .build();
    }
}
