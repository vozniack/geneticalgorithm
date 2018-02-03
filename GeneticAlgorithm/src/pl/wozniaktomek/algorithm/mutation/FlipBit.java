package pl.wozniaktomek.algorithm.mutation;

import pl.wozniaktomek.algorithm.Chromosome;

import java.util.ArrayList;

public class FlipBit extends Mutation {
    public FlipBit(ArrayList<Chromosome> population) {
        this.population = population;
        mutatePopulation();
    }

    @Override
    protected void mutateChromosome(Chromosome chromosome) {
        population.remove(chromosome);
        char[] genome = (chromosome.getStringX() + chromosome.getStringY()).toCharArray();

        for (int i = 0; i < genome.length; i++)
            if (genome[i] == '0') genome[i] = '1';
            else genome[i] = '0';

        modifyChromosome(chromosome, genome);
    }
}
