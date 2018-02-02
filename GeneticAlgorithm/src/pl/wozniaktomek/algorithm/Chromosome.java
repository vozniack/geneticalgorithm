package pl.wozniaktomek.algorithm;

public class Chromosome implements Cloneable {
    private Integer[] x;
    private Integer[] y;

    private Double minRange;
    private Double maxRange;
    private Double maxValue;

    private Double fitness;
    private Double distribution;
    private Double percent;

    private enum ValueType {X, Y}

    public Chromosome(Integer[] x, Integer[] y, Double minRange, Double maxRange) {
        this.x = x;
        this.y = y;
        this.minRange = minRange;
        this.maxRange = maxRange;
        countProperties();
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

    public Double getValueX() {
        return minRange + ((maxRange - minRange) * countValue(ValueType.X) / maxValue);
    }

    public Double getValueY() {
        return minRange + ((maxRange - minRange) * countValue(ValueType.Y) / maxValue);
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

    @Override
    protected Chromosome clone() throws CloneNotSupportedException {
        Chromosome clone;

        try {
            clone = (Chromosome)super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }

        return clone;
    }
}
