package pl.wozniaktomek.algorithm.mutation;

import pl.wozniaktomek.algorithm.Chromosome;

import java.util.ArrayList;

abstract class Mutation {
    ArrayList<Chromosome> population;
    Integer probability;

    protected abstract void mutateChromosome(Chromosome chromosome);

    void mutatePopulation() {
        for (Chromosome chromosome : population)
            mutateChromosome(chromosome);
    }

    void modifyChromosome(Chromosome chromosome, char[] genome) {
        Integer chromosomeSize = genome.length / 2;
        Integer[] x = new Integer[chromosomeSize];
        Integer[] y = new Integer[chromosomeSize];

        for (int i = 0; i < chromosomeSize; i++) {
            x[i] = (int) genome[i];
            y[i] = (int) genome[i + chromosomeSize];
        }

        chromosome.setValueX(x);
        chromosome.setValueY(y);
        population.add(chromosome);
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

}
