package pl.wozniaktomek.algorithm.crossover;

import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class DoublePoint extends Crossover {

    public DoublePoint(ArrayList<Chromosome> population, Integer probability, GeneticAlgorithm.FunctionSize functionSize) {
        this.population = population;
        this.probability = probability;
        this.functionSize = functionSize;
        this.minRange = population.get(0).getMinRange();
        this.maxRange = population.get(0).getMaxRange();

        crossPopulation();
    }

    @Override
    protected void crossGenome(String firstGenome, String secondGenome) {
        Integer point1 = ThreadLocalRandom.current().nextInt(0, firstGenome.length());
        Integer point2 = ThreadLocalRandom.current().nextInt(0, firstGenome.length());

        if (point2 < point1) {
            Integer tmp = point1;
            point1 = point2;
            point2 = tmp;
        }

        char[] newFirstGenome = (firstGenome.substring(0, point1) + secondGenome.substring(point1, point2) + firstGenome.substring(point2, firstGenome.length())).toCharArray();
        char[] newSecondGenome = (secondGenome.substring(0, point1) + firstGenome.substring(point1, point2) + secondGenome.substring(point2, firstGenome.length())).toCharArray();

        createChromosome(newFirstGenome);
        createChromosome(newSecondGenome);
    }
}
