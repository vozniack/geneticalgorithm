package pl.wozniaktomek.algorithm.mutation;

import pl.wozniaktomek.algorithm.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BitString extends Mutation {
    public BitString(ArrayList<Chromosome> population, Integer probability) {
        this.population = population;
        this.probability = probability;
        mutatePopulation();
    }

    @Override
    protected void mutateChromosome(Chromosome chromosome) {
        population.remove(chromosome);
        char[] genome = (chromosome.getStringX() + chromosome.getStringY()).toCharArray();

        for (int i = 0; i < genome.length; i++)
            if (ThreadLocalRandom.current().nextInt(0, 101) < probability) {
                if (genome[i] == '0') genome[i] = '1';
                else genome[i] = '0';
            }

        modifyChromosome(chromosome, genome);
    }

    @Override
    protected void modifyChromosome(Chromosome chromosome, char[] genome) {
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
}
