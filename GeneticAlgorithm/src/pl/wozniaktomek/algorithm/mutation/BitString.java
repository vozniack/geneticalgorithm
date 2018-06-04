package pl.wozniaktomek.algorithm.mutation;

import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Function Tomek Woźniak
 * @version 1.0
 */
public class BitString extends Mutation {
    public BitString(ArrayList<Chromosome> population, Integer probability, GeneticAlgorithm.FunctionSize functionSize) {
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

        Integer point = ThreadLocalRandom.current().nextInt(0, genome.length);
        if (genome[point] == '1') genome[point] = '0';
        else genome[point] = '1';

        modifyChromosome(chromosome, genome);
    }

    /*
     * Method acting not very well
    @Override
    protected void mutateChromosome(Chromosome chromosome) {
        population.remove(chromosome);
        char[] genome = (chromosome.getStringX() + chromosome.getStringY()).toCharArray();

        for (int i = 0; i < genome.length; i++)
        if (ThreadLocalRandom.current().nextInt(0, 100) <= probability) {
            if (genome[i] == '0') genome[i] = '1';
            else genome[i] = '0';
        }

        modifyChromosome(chromosome, genome);
    }
    */
}
