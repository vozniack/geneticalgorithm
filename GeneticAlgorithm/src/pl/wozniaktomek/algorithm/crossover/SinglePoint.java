package pl.wozniaktomek.algorithm.crossover;

import pl.wozniaktomek.algorithm.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SinglePoint extends Crossover {
    public SinglePoint(ArrayList<Chromosome> population, Integer probability) {
        this.population = population;
        this.probability = probability;
        this.chromosomeSize = population.get(0).getSize();
        this.minRange = population.get(0).getMinRange();
        this.maxRange = population.get(0).getMaxRange();
        crossPopulation();
    }

    @Override
    protected void crossString(String first, String second) {
        Integer point = ThreadLocalRandom.current().nextInt(0, first.length());

        for (int i = 0; i < point + 1; i++) {
            newFirst.append(first.charAt(i));
            newSecond.append(second.charAt(i));
        }

        for (int i = point + 1; i < first.length(); i++) {
            newFirst.append(second.charAt(i));
            newSecond.append(first.charAt(i));
        }

        createChromosome(newFirst.toString());
        createChromosome(newSecond.toString());
    }

    @Override
    protected void createChromosome(String genome) {
        Integer chromosomeSize = genome.length() / 2;
        Integer[] x = new Integer[chromosomeSize];
        Integer[] y = new Integer[chromosomeSize];

        for (int i = 0; i < chromosomeSize; i++) {
            x[i] = (int) genome.charAt(i);
            y[i] = (int) genome.charAt(i + chromosomeSize);
        }

        addChromosome(x, y);
    }
}
