package pl.wozniaktomek.algorithm;

import pl.wozniaktomek.GeneticAlgorithmApp;
import pl.wozniaktomek.algorithm.components.Chromosome;
import pl.wozniaktomek.algorithm.components.Generate;
import pl.wozniaktomek.algorithm.crossover.DoublePoint;
import pl.wozniaktomek.algorithm.crossover.SinglePoint;
import pl.wozniaktomek.algorithm.mutation.BitString;
import pl.wozniaktomek.algorithm.mutation.FlipBit;
import pl.wozniaktomek.algorithm.selection.Roulette;
import pl.wozniaktomek.algorithm.selection.Tournament;

import java.util.ArrayList;

public class GeneticAlgorithm extends Thread {
    /* Operations */
    public enum SelectionMethod {ROULETTE, TOURNAMENT, RANKING}
    public enum CrossoverMethod {SINGLE, DOUBLE}
    public enum MutationMethod {BITSTRING, FLIPBIT}

    /* Function */
    public enum FunctionInstance {F1, F2, F3, F4};
    public enum FunctionType {MIN, MAX};
    public enum FunctionSize {V1, V2};

    /* Initial data */
    private Integer generationsAmount, probabilityCrossover, probabilityMutation, tournamentSize;
    private SelectionMethod selectionMethod;
    private CrossoverMethod crossoverMethod;
    private MutationMethod mutationMethod;
    private FunctionInstance functionInstance;
    private FunctionType functionType;
    private FunctionSize functionSize;

    /* Operational data */
    private ArrayList<Chromosome> currentPopulation;
    private volatile Integer generationCounter;
    private volatile Boolean isRunning;
    private volatile Boolean isChart;
    private volatile Boolean isUpdating;

    /* Initializing methods */
    public GeneticAlgorithm(Integer populationSize, Integer chromosomeSize, Integer generationsAmount, Integer probabilityCrossover, Integer probabilityMutation, Double minRange, Double maxRange) {
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

    public void setFunction(FunctionInstance functionInstance, FunctionType functionType, FunctionSize functionSize) {
        this.functionInstance = functionInstance;
        this.functionType = functionType;
        this.functionSize = functionSize;
    }

    public void setTournamentSize(Integer tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    private void generate(Integer populationSize, Integer chromosomeSize, Double minRange, Double maxRange) {
        currentPopulation = new Generate(populationSize, chromosomeSize, minRange, maxRange).getPopulation();
    }

    /* Control methods */
    public void setRunning(Boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setChart(Boolean isChart) {
        this.isChart = isChart;
    }

    public void setUpdating(Boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    private void updateUI() {
        GeneticAlgorithmApp.windowControl.updateGeneration(generationCounter);
        // System.out.println("## Just updated counter");

        if (isChart) {
            GeneticAlgorithmApp.windowControl.updatePopulation(currentPopulation);
            // System.out.println("## Just updated population");
            isUpdating = true;

            Integer counter = 0;
            while (isUpdating) try {
                if (counter > 10)
                    isUpdating = false;
                // System.out.println("## Waiting for update population");
                counter++;
                Thread.sleep(25);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    /* Algorithm methods */
    private void startAlgorithm() {
        generationCounter = 0;
        while (isRunning) {
            if (generationCounter.equals(generationsAmount))
                break;

            if (checkPopulation()) {
                // System.out.println("Selection");
                selection();

                // System.out.println("Crossover");
                crossover();

                // System.out.println("Mutation");
                mutation();

                // System.out.println("Updating");
                generationCounter++;
                updateUI();
            }

            else break;
        }

        updateUI();
        finishAlgorithm();
    }

    private void finishAlgorithm() {
        GeneticAlgorithmApp.windowControl.finishAlgorithm(currentPopulation);
    }

    private void selection() {
        switch (selectionMethod) {
            case ROULETTE:
                clonePopulation(new Roulette(currentPopulation, functionInstance, functionType, functionSize).getPopulation());
                break;

            case TOURNAMENT:
                clonePopulation(new Tournament(currentPopulation, functionInstance, functionType, functionSize, tournamentSize).getPopulation());
                break;
        }
    }

    private void crossover() {
        switch (crossoverMethod) {
            case SINGLE:
                clonePopulation(new SinglePoint(currentPopulation, probabilityCrossover, functionSize).getPopulation());
                break;

            case DOUBLE:
                clonePopulation(new DoublePoint(currentPopulation, probabilityCrossover, functionSize).getPopulation());
                break;
        }
    }

    private void mutation() {
        switch (mutationMethod) {
            case BITSTRING:
                clonePopulation(new BitString(currentPopulation, probabilityMutation, functionSize).getPopulation());
                break;

            case FLIPBIT:
                clonePopulation(new FlipBit(currentPopulation, probabilityMutation, functionSize).getPopulation());
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

    public FunctionSize getFunctionSize() {
        return this.functionSize;
    }

    @Override
    public void run() {
        isRunning = true;
        startAlgorithm();
    }
}