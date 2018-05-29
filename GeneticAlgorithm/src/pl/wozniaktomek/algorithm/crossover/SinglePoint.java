package pl.wozniaktomek.algorithm.crossover;

import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class SinglePoint extends Crossover {
    public SinglePoint(ArrayList<Chromosome> population, Integer probability, GeneticAlgorithm.FunctionSize functionSize) {
        this.population = population;
        this.probability = probability;
        this.functionSize = functionSize;
        this.minRange = population.get(0).getMinRange();
        this.maxRange = population.get(0).getMaxRange();

        crossPopulation();
    }

    @Override
    protected void crossGenome(String firstGenome, String secondGenome) {
        Integer point = ThreadLocalRandom.current().nextInt(0, firstGenome.length());

        char[] newFirstGenome = (firstGenome.substring(0, point) + secondGenome.substring(point, firstGenome.length())).toCharArray();
        char[] newSecondGenome = (secondGenome.substring(0, point) + firstGenome.substring(point, firstGenome.length())).toCharArray();

        createChromosome(newFirstGenome);
        createChromosome(newSecondGenome);
    }
}
