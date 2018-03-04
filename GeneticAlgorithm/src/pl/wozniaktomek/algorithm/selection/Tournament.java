package pl.wozniaktomek.algorithm.selection;

import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Tournament extends Selection {
    private Integer tournamentSize;

    public Tournament(ArrayList<Chromosome> oldPopulation, Integer tournamentSize) {
        this.oldPopulation = oldPopulation;
        this.tournamentSize = tournamentSize;
        countFitness();
        selectPopulation();
    }

    @Override
    protected void selectPopulation() {
        newPopulation = new ArrayList<>();

        for (int i = 0; i < oldPopulation.size(); i++)
            newPopulation.add(tournament());
    }

    private Chromosome tournament() {
        ArrayList<Chromosome> tmpPopulation = new ArrayList<>();

        for (int i = 0; i < tournamentSize; i++)
            tmpPopulation.add(selectChromosome());

        sortPopulation(tmpPopulation, false);
        return tmpPopulation.get(0);
    }

    private Chromosome selectChromosome() {
        return oldPopulation.get(ThreadLocalRandom.current().nextInt(0, oldPopulation.size()));
    }
}
