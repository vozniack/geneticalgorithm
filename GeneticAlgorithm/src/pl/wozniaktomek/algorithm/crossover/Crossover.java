package pl.wozniaktomek.algorithm.crossover;

import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

abstract class Crossover {
    ArrayList<Chromosome> population;
    Integer probability;
    Double minRange, maxRange;
    GeneticAlgorithm.FunctionSize functionSize;

    protected abstract void crossGenome(String firstGenome, String secondGenome);

    void crossPopulation() {
        for (int i = 0; i < population.size(); i++)
            if (ThreadLocalRandom.current().nextInt(0, 100) < probability)
                crossChromosomes();
    }

    private void crossChromosomes() {
        Chromosome firstChromosome = selectChromosome();
        Chromosome secondChromosome = selectChromosome();

        if (functionSize == GeneticAlgorithm.FunctionSize.V1)
            crossGenome(firstChromosome.getStringX(), secondChromosome.getStringX());
        else crossGenome(firstChromosome.getStringX() + firstChromosome.getStringY(), secondChromosome.getStringX() + secondChromosome.getStringY());
    }

    private Chromosome selectChromosome() {
        Chromosome chromosome = population.get(ThreadLocalRandom.current().nextInt(0, population.size()));
        population.remove(chromosome);
        return chromosome;
    }

    void createChromosome(char[] genome) {
        if (functionSize == GeneticAlgorithm.FunctionSize.V1)
            createChromosomeSingle(genome);
        else createChromosomeDouble(genome);
    }

    private void createChromosomeSingle(char[] genome) {
        Integer[] x = new Integer[genome.length];

        for (int i = 0; i < genome.length; i++)
            x[i] = (Integer.valueOf(String.valueOf(genome[i])));

        addChromosome(x);
    }

    private void createChromosomeDouble(char[] genome) {
        Integer[] x = new Integer[genome.length / 2];
        Integer[] y = new Integer[genome.length / 2];

        for (int i = 0; i < (genome.length / 2); i++) {
            x[i] = (Integer.valueOf(String.valueOf(genome[i])));
            y[i] = (Integer.valueOf(String.valueOf(genome[i + (genome.length / 2)])));
        }

        addChromosome(x, y);
    }

    private void addChromosome(Integer[] x) {
        population.add(new Chromosome(x, minRange, maxRange));
    }

    private void addChromosome(Integer[] x, Integer[] y) {
        population.add(new Chromosome(x, y, minRange, maxRange));
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }
}
