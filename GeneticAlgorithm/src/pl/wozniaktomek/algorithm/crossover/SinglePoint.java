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
    protected void crossGenome(String firstGenome, String secondGenome) {
        Integer point = ThreadLocalRandom.current().nextInt(0, firstGenome.length());

        char[] newFirstGenome = (firstGenome.substring(0, point) + secondGenome.substring(point, firstGenome.length())).toCharArray();
        char[] newSecondGenome = (secondGenome.substring(0, point) + firstGenome.substring(point, firstGenome.length())).toCharArray();

        createChromosome(newFirstGenome);
        createChromosome(newSecondGenome);
    }

    @Override
    protected void createChromosome(char[] genome) {
        Integer chromosomeSize = genome.length / 2;
        Integer[] x = new Integer[chromosomeSize];
        Integer[] y = new Integer[chromosomeSize];

        for (int i = 0; i < chromosomeSize; i++) {
            x[i] = (Integer.valueOf(String.valueOf(genome[i])));
            y[i] = (Integer.valueOf(String.valueOf(genome[i + chromosomeSize])));
        }

        addChromosome(x, y);
    }
}
