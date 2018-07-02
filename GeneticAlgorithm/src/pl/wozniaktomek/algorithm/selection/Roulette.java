package pl.wozniaktomek.algorithm.selection;

import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Tomek Woźniak
 * @version 1.0
 */
public class Roulette extends Selection {
    private Double sumOfDistribution = 0d;
    private Double sumOfFitness = 0d;

    public Roulette(ArrayList<Chromosome> oldPopulation, GeneticAlgorithm.FunctionInstance functionInstance, GeneticAlgorithm.FunctionType functionType, GeneticAlgorithm.FunctionSize functionSize) {
        this.oldPopulation = oldPopulation;
        this.functionInstance = functionInstance;
        this.functionType = functionType;
        this.functionSize = functionSize;

        countFitness();

        if (functionType == GeneticAlgorithm.FunctionType.MIN)
            sortPopulation(oldPopulation, true);
        else sortPopulation(oldPopulation, false);

        countDistribution();
        countPercent();
        countSumOfPercent();
        selectPopulation();
    }

    private void countDistribution() {
        if (functionType == GeneticAlgorithm.FunctionType.MIN) {
            Double bestFitness = oldPopulation.get(0).getFitness();
            for (Chromosome chromosome : oldPopulation) {
                chromosome.setDistribution(bestFitness - chromosome.getFitness() + 1.0);
                sumOfDistribution += chromosome.getDistribution();
            }
        }

        else {
            for (Chromosome chromosome : oldPopulation)
                sumOfFitness += chromosome.getFitness();
        }
    }

    private void countPercent() {
        if (functionType == GeneticAlgorithm.FunctionType.MIN)
            for (Chromosome chromosome : oldPopulation)
                chromosome.setPercent((chromosome.getDistribution() / sumOfDistribution) * 100.0);

        else
            for (Chromosome chromosome : oldPopulation)
                chromosome.setPercent((chromosome.getFitness() / sumOfFitness) * 100.0);
    }

    private void countSumOfPercent() {
        Double sumOfPercent = oldPopulation.get(0).getPercent();
        for (int i = 1; i < oldPopulation.size(); i++) {
            sumOfPercent += oldPopulation.get(i).getPercent();
            oldPopulation.get(i).setPercent(sumOfPercent);
        }
    }

    @Override
    protected void selectPopulation() {
        newPopulation = new ArrayList<>();
        Integer counter = 0;

        while (counter != oldPopulation.size()) {
            Double roulette = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
            for (int i = 0; i < oldPopulation.size() - 1; i++) {
                if (roulette >= oldPopulation.get(i).getPercent() && roulette <= oldPopulation.get(i + 1).getPercent()) {
                    if (oldPopulation.get(i + 1).getPercent() < (roulette - oldPopulation.get(i).getPercent()))
                        newPopulation.add(oldPopulation.get(i));
                    else newPopulation.add(oldPopulation.get(i + 1));
                    counter++;
                }
            }
        }
    }
}
