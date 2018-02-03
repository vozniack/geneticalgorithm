package pl.wozniaktomek.algorithm;

public class Function {
    public Double getResult(Double x, Double y) {
        return 100 * Math.pow((Math.pow(x, 2.0) - y), 2.0) + Math.pow((1 - x), 2.0);
    }
}
