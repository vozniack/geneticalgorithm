package pl.wozniaktomek.algorithm.components;

public class Function {
    // Minimization function z = 2x^2 + 2y^2 - 4, min in (x, y) = (0, 0), f(0, 0) = -4
    public Double getResult(Double x, Double y) {
        return 2 * Math.pow(x, 2d) + 2 * Math.pow(y, 2d) - 4;
    }
}
