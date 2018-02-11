package pl.wozniaktomek.algorithm.mutation;

import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;

abstract class Mutation {
    ArrayList<Chromosome> population;
    Integer probability;

    protected abstract void mutateChromosome(Chromosome chromosome);

    void mutatePopulation() {
        for (int i = 0; i < population.size(); i++)
            mutateChromosome(population.get(i));
    }

    void modifyChromosome(Chromosome chromosome, char[] genome) {
        Integer chromosomeSize = genome.length / 2;
        Integer[] x = new Integer[chromosomeSize];
        Integer[] y = new Integer[chromosomeSize];

        for (int i = 0; i < chromosomeSize; i++) {
            x[i] = (Integer.valueOf(String.valueOf(genome[i])));
            y[i] = (Integer.valueOf(String.valueOf(genome[i + chromosomeSize])));
        }

        chromosome.setValueX(x);
        chromosome.setValueY(y);
        population.add(chromosome);
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

}
