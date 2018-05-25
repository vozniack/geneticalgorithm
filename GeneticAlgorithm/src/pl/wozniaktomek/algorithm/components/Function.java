package pl.wozniaktomek.algorithm.components;

import pl.wozniaktomek.algorithm.GeneticAlgorithm;

public class Function {
    private GeneticAlgorithm.FunctionInstance functionInstance;

    public Function(GeneticAlgorithm.FunctionInstance functionInstance) {
        this.functionInstance = functionInstance;
    }

    public Double getResult(Double x, Double y) {
        // f(x,y) = 2x^2 + 2y^2 - 4 // result in f(0, 0)
        if (functionInstance == GeneticAlgorithm.FunctionInstance.F1)
            return 2*Math.pow(x, 2d) + 2*Math.pow(y, 2d) - 4;

        // f(x,y) = 5 + 3x - 4y - x^2 + xy - y^2 // result in f(2/3, -5/3)
        else if (functionInstance == GeneticAlgorithm.FunctionInstance.F2) {
            return 5 + 3*x - 4*y - Math.pow(x, 2d) + x*y - Math.pow(y, 2d);
        }

        else return 0D;
    }

    public Double getResult(Double x) {
        // f(x) = x^2 - 2x + 3 // result in f(1)
        if (functionInstance == GeneticAlgorithm.FunctionInstance.F3)
            return Math.pow(x, 2d) - (2 * x) + 3;

        else if (functionInstance == GeneticAlgorithm.FunctionInstance.F4) {
            return 1D;
        }

        else return 0D;
    }
}
