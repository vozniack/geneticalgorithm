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

/**
 * @author Tomek Woźniak
 * @version 1.0
 */
public class GeneticAlgorithm extends Thread {
    /* Operations */
    public enum SelectionMethod {ROULETTE, TOURNAMENT}
    public enum CrossoverMethod {SINGLE, DOUBLE}
    public enum MutationMethod {BITSTRING, FLIPBIT}

    /* Function */
    public enum FunctionInstance {F1, F2, F3, F4}
    public enum FunctionType {MIN, MAX}
    public enum FunctionSize {V1, V2}

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
    private Integer generationCounter;
    private volatile Boolean isRunning;
    private volatile Boolean isChart;
    private volatile Boolean isUpdating;

    /* Initializing methods */
    public void createPopulation(Integer populationSize, Integer chromosomeSize, Double minRange, Double maxRange) {
        generate(populationSize, chromosomeSize, minRange, maxRange);
    }

    public void setProbabilities(Integer probabilityCrossover, Integer probabilityMutation) {
        this.probabilityCrossover = probabilityCrossover;
        this.probabilityMutation = probabilityMutation;
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

    public void setGenerationsAmount(Integer generationsAmount) {
        this.generationsAmount = generationsAmount;
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

        if (isChart) {
            GeneticAlgorithmApp.windowControl.updatePopulation(currentPopulation);
            isUpdating = true;

            Integer counter = 0;
            while (isUpdating) try {
                if (counter > 10)
                    isUpdating = false;
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
                selection();
                crossover();
                mutation();
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
            currentPopulation.add(chromosome.clone());
    }

    public ArrayList<Chromosome> getCurrentPopulation() {
        return this.currentPopulation;
    }

    public FunctionInstance getFunctionInstance() {
        return this.functionInstance;
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