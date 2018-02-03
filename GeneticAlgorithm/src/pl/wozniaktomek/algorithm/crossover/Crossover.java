package pl.wozniaktomek.algorithm.crossover;

import pl.wozniaktomek.algorithm.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

abstract class Crossover {
    ArrayList<Chromosome> population;
    Integer probability, chromosomeSize;
    Double minRange, maxRange;
    StringBuilder newFirst = new StringBuilder();
    StringBuilder newSecond = new StringBuilder();

    protected abstract void crossString(String first, String second);
    protected abstract void createChromosome(String genome);

    void crossPopulation() {
        for (int i = 0; i < population.size(); i++)
            if (ThreadLocalRandom.current().nextInt(0, 101) < probability)
                crossChromosomes();
    }

    private void crossChromosomes() {
        Chromosome firstChromosome = selectChromosome();
        Chromosome secondChromosome = selectChromosome();

        String first = firstChromosome.getStringX() + firstChromosome.getStringY();
        String second = secondChromosome.getStringX() + secondChromosome.getStringY();
        crossString(first, second);
    }

    private Chromosome selectChromosome() {
        Chromosome chromosome = population.get(ThreadLocalRandom.current().nextInt(0, population.size()));
        population.remove(chromosome);
        return chromosome;
    }

    void addChromosome(Integer[] x, Integer[] y) {
        population.add(new Chromosome(x, y, minRange, maxRange));
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }
}
