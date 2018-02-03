package pl.wozniaktomek.algorithm;

import pl.wozniaktomek.algorithm.crossover.SinglePoint;
import pl.wozniaktomek.algorithm.mutation.BitString;
import pl.wozniaktomek.algorithm.mutation.FlipBit;
import pl.wozniaktomek.algorithm.selection.Roulette;

import java.util.ArrayList;

public class GeneticAlgorithm implements Runnable {
    /* Operations */
    private enum SelectionMethod {ROULETTE, TOURNAMENT, RANKING}
    private enum CrossoverMethod {SINGLE, DOUBLE, MULTI}
    private enum MutationMethod {BITSTRING, FLIPBIT}

    /* Initial data */
    private Integer generationsAmount, probabilityCrossover, probabilityMutation;
    private SelectionMethod selectionMethod;
    private CrossoverMethod crossoverMethod;
    private MutationMethod mutationMethod;

    /* Operational data */
    private ArrayList<Chromosome> currentPopulation;
    private volatile Boolean isRunning;

    /* Initializing methods */
    GeneticAlgorithm(Integer populationSize, Integer chromosomeSize, Integer generationsAmount, Integer probabilityCrossover, Integer probabilityMutation, Double minRange, Double maxRange) {
        this.generationsAmount = generationsAmount;
        this.probabilityCrossover = probabilityCrossover;
        this.probabilityMutation = probabilityMutation;
        generate(populationSize, chromosomeSize, minRange, maxRange);
    }

    public void setMethods(SelectionMethod selectionMethod, CrossoverMethod crossoverMethod, MutationMethod mutationMethod) {
        this.selectionMethod = selectionMethod;
        this.crossoverMethod = crossoverMethod;
        this.mutationMethod = mutationMethod;
    }

    private void generate(Integer populationSize, Integer chromosomeSize, Double minRange, Double maxRange) {
        currentPopulation = new Generate(populationSize, chromosomeSize, minRange, maxRange).getPopulation();
    }

    /* Control methods */
    public void setRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    /* Algorithm methods */
    private void startAlgorithm() {
        Integer counter = 0;
        while (isRunning) {
            if (counter.equals(generationsAmount)) break;

            if (checkPopulation()) {
                selection();
                crossover();
                mutation();
                counter++;
            }

            else break;
        }

        stopAlgorithm();
    }

    private void stopAlgorithm() {
        // TODO
    }

    private void selection() {
        switch (selectionMethod) {
            case ROULETTE:
                clonePopulation(new Roulette(currentPopulation).getPopulation());
                break;

            case TOURNAMENT:
                break;

            case RANKING:
                break;
        }
    }

    private void crossover() {
        switch (crossoverMethod) {
            case SINGLE:
                clonePopulation(new SinglePoint(currentPopulation, probabilityCrossover).getPopulation());
                break;

            case DOUBLE:
                break;

            case MULTI:
                break;
        }
    }

    private void mutation() {
        switch (mutationMethod) {
            case BITSTRING:
                clonePopulation(new BitString(currentPopulation, probabilityMutation).getPopulation());
                break;

            case FLIPBIT:
                clonePopulation(new FlipBit(currentPopulation).getPopulation());
                break;
        }
    }

    private Boolean checkPopulation() {
        for (int i = 0; i < currentPopulation.size() - 1; i++)
            if (!currentPopulation.get(i).equals(currentPopulation.get(i + 1)))
                return true;
        return false;
    }

    private void clonePopulation(ArrayList<Chromosome> newPopulation) {
        currentPopulation = new ArrayList<>(newPopulation.size());
        for (Chromosome chromosome : newPopulation)
            try {
                currentPopulation.add(chromosome.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void run() {
        startAlgorithm();
    }
}