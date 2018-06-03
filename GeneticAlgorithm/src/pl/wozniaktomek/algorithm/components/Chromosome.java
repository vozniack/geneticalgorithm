package pl.wozniaktomek.algorithm.components;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class Chromosome implements Cloneable {
    private Integer[] x;
    private Integer[] y;

    private Double minRange;
    private Double maxRange;
    private Double maxValue;

    private Double fitness;
    private Double distribution;
    private Double percent;

    private Double smallApproximation;
    private Double bigApproximation;

    private enum ValueType {X, Y}

    public Chromosome(Integer[] x, Integer[] y, Double minRange, Double maxRange) {
        this.x = x;
        this.y = y;
        this.minRange = minRange;
        this.maxRange = maxRange;
        countProperties();
    }

    public Chromosome(Integer[] x, Double minRange, Double maxRange) {
        this.x = x;
        this.minRange = minRange;
        this.maxRange = maxRange;
        countProperties();
    }

    /* Calculation methods */
    public void countApproximation() {
        smallApproximation = BigDecimal.valueOf(fitness).setScale(3, RoundingMode.HALF_UP).doubleValue();
        bigApproximation = BigDecimal.valueOf(fitness).setScale(9, RoundingMode.HALF_UP).doubleValue();
    }

    private void countProperties() {
        maxValue = 0d;
        for (int i = 0; i < x.length; i++)
            maxValue += (int) (Math.pow(2, i));
    }

    private Double countValue(ValueType valueType) {
        Double value = 0d;
        Double counter = 0d;

        if (valueType == ValueType.X)
            for (int i = x.length - 1; i >= 0; i--) {
                if (x[i].equals(1))
                    value += Math.pow(2, counter);
                counter++;
            }

        if (valueType == ValueType.Y)
            for (int i = y.length - 1; i >= 0; i--) {
                if (y[i].equals(1))
                    value += Math.pow(2, counter);
                counter++;
            }

        return value;
    }

    /* Getters & setters */
    public Double getValueX() {
        return minRange + ((maxRange - minRange) * countValue(ValueType.X) / maxValue);
    }

    public Double getValueY() {
        return minRange + ((maxRange - minRange) * countValue(ValueType.Y) / maxValue);
    }

    public void setValueX(Integer[] x) {
        this.x = x;
    }

    public void setValueY(Integer[] y) {
        this.y = y;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }

    public void setDistribution(Double distribution) {
        this.distribution = distribution;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Double getFitness() {
        return fitness;
    }

    public Double getDistribution() {
        return distribution;
    }

    public Double getPercent() {
        return percent;
    }

    public Double getSmallApproximation() {
        return this.smallApproximation;
    }

    public Double getBigApproximation() {
        return this.bigApproximation;
    }

    public String getStringX() {
        StringBuilder string = new StringBuilder();
        for (Integer X : x) string.append(String.valueOf(X));
        return string.toString();
    }

    public String getStringY() {
        StringBuilder string = new StringBuilder();
        for (Integer Y : y) string.append(String.valueOf(Y));
        return string.toString();
    }

    public Double getMinRange() {
        return minRange;
    }

    public Double getMaxRange() {
        return maxRange;
    }

    public Integer getSize() {
        return x.length;
    }

    /* Comparing */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chromosome that = (Chromosome) o;

        return Arrays.equals(x, that.x) && Arrays.equals(y, that.y);
    }

    /* Cloning */
    @Override
    public Chromosome clone() throws CloneNotSupportedException {
        Chromosome clone;

        try {
            clone = (Chromosome)super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }

        return clone;
    }
}
