package pl.wozniaktomek.algorithm.mutation;

import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;

public class FlipBit extends Mutation {
    public FlipBit(ArrayList<Chromosome> population, Integer probability, GeneticAlgorithm.FunctionSize functionSize) {
        this.population = population;
        this.probability = probability;
        this.functionSize = functionSize;
        mutatePopulation();
    }

    @Override
    protected void mutateChromosome(Chromosome chromosome) {
        population.remove(chromosome);

        char[] genome;
        if (functionSize == GeneticAlgorithm.FunctionSize.V1)
            genome = chromosome.getStringX().toCharArray();
        else genome = (chromosome.getStringX() + chromosome.getStringY()).toCharArray();

        for (int i = 0; i < genome.length; i++)
            if (genome[i] == '0') genome[i] = '1';
            else genome[i] = '0';

        modifyChromosome(chromosome, genome);
    }
}
