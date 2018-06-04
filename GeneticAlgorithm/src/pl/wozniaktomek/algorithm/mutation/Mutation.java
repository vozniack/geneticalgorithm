package pl.wozniaktomek.algorithm.mutation;

import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Function Tomek Woźniak
 * @version 1.0
 */
abstract class Mutation {
    ArrayList<Chromosome> population;
    Integer probability;
    GeneticAlgorithm.FunctionSize functionSize;

    protected abstract void mutateChromosome(Chromosome chromosome);

    void mutatePopulation() {
        for (int i = 0; i < population.size(); i++) {
            if (ThreadLocalRandom.current().nextInt(0, 100) <= probability)
                mutateChromosome(population.get(i));
        }
    }

    void modifyChromosome(Chromosome chromosome, char[] genome) {
        if (functionSize == GeneticAlgorithm.FunctionSize.V1)
            modifyChromosomeSingle(chromosome, genome);
        else modifyChromosomeDouble(chromosome, genome);
    }

    private void modifyChromosomeSingle(Chromosome chromosome, char[] genome) {
        Integer[] x = new Integer[genome.length];

        for (int i = 0; i < genome.length; i++)
            x[i] = (Integer.valueOf(String.valueOf(genome[i])));

        chromosome.setValueX(x);
        population.add(chromosome);
    }

    private void modifyChromosomeDouble(Chromosome chromosome, char[] genome) {
        Integer[] x = new Integer[genome.length / 2];
        Integer[] y = new Integer[genome.length / 2];

        for (int i = 0; i < genome.length / 2; i++) {
            x[i] = (Integer.valueOf(String.valueOf(genome[i])));
            y[i] = (Integer.valueOf(String.valueOf(genome[i + genome.length / 2])));
        }

        chromosome.setValueX(x);
        chromosome.setValueY(y);
        population.add(chromosome);
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

}
