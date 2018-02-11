package pl.wozniaktomek.algorithm.selection;

import pl.wozniaktomek.algorithm.components.Chromosome;
import pl.wozniaktomek.algorithm.components.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

abstract class Selection {
    ArrayList<Chromosome> oldPopulation;
    ArrayList<Chromosome> newPopulation;

    protected abstract void selectPopulation();

    void countFitness() {
        for (Chromosome chromosome : oldPopulation)
            chromosome.setFitness(new Function().getResult(chromosome.getValueX(), chromosome.getValueY()));
    }

    void sortPopulation(Boolean isAsc) {
        try {
            if (isAsc) oldPopulation.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
            else oldPopulation.sort(Comparator.comparingDouble(Chromosome::getFitness));
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            // TODO STOP ALGORITHM
        }
    }

     public ArrayList<Chromosome> getPopulation() {
         return newPopulation;
     }
}
