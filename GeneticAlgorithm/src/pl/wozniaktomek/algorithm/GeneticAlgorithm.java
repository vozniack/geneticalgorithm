package pl.wozniaktomek.algorithm;

import java.util.ArrayList;

public class GeneticAlgorithm implements Runnable {
    /* Operations */
    private enum SelectionMethod {ROULETTE, TOURNAMENT, RANKING}
    private enum CrossoverMethod {SINGLE, DOUBLE, MULTI}
    private enum MutationMethod {BITSTRING, FLIPBIT}

    /* Initial data */
    private Integer generationsAmount;
    private Double probabilityCrossover, probabilityMutation;
    private SelectionMethod selectionMethod;
    private CrossoverMethod crossoverMethod;
    private MutationMethod mutationMethod;

    /* Operational data */
    private ArrayList<Chromosome> currentPopulation;
    private volatile Boolean isRunning;

    /* Initializing methods */
    GeneticAlgorithm(Integer populationSize, Integer chromosomeSize, Integer generationsAmount, Double probabilityCrossover, Double probabilityMutation, Double minRange, Double maxRange) {
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
        // TODO
    }

    private void crossover() {
        // TODO
    }

    private void mutation() {
        // TODO
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