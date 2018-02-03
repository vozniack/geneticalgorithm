package pl.wozniaktomek.algorithm;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

class Generate {
    private Integer populationSize, chromosomeSize;
    private Double minRange, maxRange;
    private ArrayList<Chromosome> population;

    Generate(Integer populationSize, Integer chromosomeSize, Double minRange, Double maxRange) {
        this.populationSize = populationSize;
        this.chromosomeSize = chromosomeSize;
        this.minRange = minRange;
        this.maxRange = maxRange;
        generatePopulation();
    }

    private void generatePopulation() {
        population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++)
            population.add(generateChromosome());
    }

    private Chromosome generateChromosome() {
        Integer x[] = new Integer[chromosomeSize];
        Integer y[] = new Integer[chromosomeSize];

        for (int i = 0; i < chromosomeSize; i++) {
            x[i] = ThreadLocalRandom.current().nextInt(0, 2);
            y[i] = ThreadLocalRandom.current().nextInt(0, 2);
        }

        return new Chromosome(x, y, minRange, maxRange);
    }

    ArrayList<Chromosome> getPopulation() {
        return population;
    }
}
