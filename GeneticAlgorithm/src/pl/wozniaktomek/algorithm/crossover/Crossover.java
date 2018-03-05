package pl.wozniaktomek.algorithm.crossover;

import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

abstract class Crossover {
    ArrayList<Chromosome> population;
    Integer probability, chromosomeSize;
    Double minRange, maxRange;

    protected abstract void crossGenome(String firstGenome, String secondGenome);

    void crossPopulation() {
        for (int i = 0; i < population.size(); i++)
            if (ThreadLocalRandom.current().nextInt(0, 100) < probability)
                crossChromosomes();
    }

    private void crossChromosomes() {
        Chromosome firstChromosome = selectChromosome();
        Chromosome secondChromosome = selectChromosome();

        String firstGenome = firstChromosome.getStringX() + firstChromosome.getStringY();
        String secondGenome = secondChromosome.getStringX() + secondChromosome.getStringY();
        crossGenome(firstGenome, secondGenome);
    }

    private Chromosome selectChromosome() {
        Chromosome chromosome = population.get(ThreadLocalRandom.current().nextInt(0, population.size()));
        population.remove(chromosome);
        return chromosome;
    }

    void createChromosome(char[] genome) {
        Integer chromosomeSize = genome.length / 2;
        Integer[] x = new Integer[chromosomeSize];
        Integer[] y = new Integer[chromosomeSize];

        for (int i = 0; i < chromosomeSize; i++) {
            x[i] = (Integer.valueOf(String.valueOf(genome[i])));
            y[i] = (Integer.valueOf(String.valueOf(genome[i + chromosomeSize])));
        }

        addChromosome(x, y);
    }

    private void addChromosome(Integer[] x, Integer[] y) {
        population.add(new Chromosome(x, y, minRange, maxRange));
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }
}
