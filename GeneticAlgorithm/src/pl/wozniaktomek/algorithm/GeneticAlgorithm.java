package pl.wozniaktomek.algorithm;

import pl.wozniaktomek.GeneticAlgorithmApp;
import pl.wozniaktomek.algorithm.components.Chromosome;
import pl.wozniaktomek.algorithm.components.Function;
import pl.wozniaktomek.algorithm.components.Generate;
import pl.wozniaktomek.algorithm.crossover.SinglePoint;
import pl.wozniaktomek.algorithm.mutation.BitString;
import pl.wozniaktomek.algorithm.mutation.FlipBit;
import pl.wozniaktomek.algorithm.selection.Roulette;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GeneticAlgorithm extends Thread {
    /* Operations */
    public enum SelectionMethod {ROULETTE, TOURNAMENT, RANKING}
    public enum CrossoverMethod {SINGLE, DOUBLE}
    public enum MutationMethod {BITSTRING, FLIPBIT}

    /* Initial data */
    private Integer generationsAmount, probabilityCrossover, probabilityMutation;
    private SelectionMethod selectionMethod;
    private CrossoverMethod crossoverMethod;
    private MutationMethod mutationMethod;

    /* Operational data */
    private ArrayList<Chromosome> currentPopulation;
    private volatile Integer generationCunter;
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
        GeneticAlgorithmApp.windowControl.updateGeneration(generationCunter);
        System.out.println("## Just updated counter");

        if (isChart) {
            GeneticAlgorithmApp.windowControl.updatePopulation(currentPopulation);
            System.out.println("## Just updated population");
            isUpdating = true;

            Integer counter = 0;
            while (isUpdating) try {
                if (counter > 10) isUpdating = false;
                System.out.println("## Waiting for update population");
                counter++;
                Thread.sleep(25);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    /* Algorithm methods */
    private void startAlgorithm() {
        generationCunter = 0;
        while (isRunning) {
            if (generationCunter.equals(generationsAmount)) break;

            if (checkPopulation()) {
                System.out.println("Selection");
                selection();

                System.out.println("Crossover");
                crossover();

                System.out.println("Mutation");
                mutation();

                System.out.println("Updating");

                generationCunter++;
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
        }
    }

    private void mutation() {
        switch (mutationMethod) {
            case BITSTRING:
                clonePopulation(new BitString(currentPopulation, probabilityMutation).getPopulation());
                break;

            case FLIPBIT:
                clonePopulation(new FlipBit(currentPopulation, probabilityMutation).getPopulation());
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

    /* Only for test */
    private void showPopulation() {
        for (Chromosome chromosome : currentPopulation)
            System.out.println("[x] = " + chromosome.getValueX() + " [y] = " + chromosome.getValueY() + " [f(x,y)] = " +
                    new Function().getResult(chromosome.getValueX(), chromosome.getValueY()) +
                    " | " + chromosome.getStringX() + " " + chromosome.getStringY() + " | " + chromosome.getFitness());
    }

    @Override
    public void run() {
        isRunning = true;
        startAlgorithm();
    }
}