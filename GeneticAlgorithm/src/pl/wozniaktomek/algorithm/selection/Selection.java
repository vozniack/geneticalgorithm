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

    void sortPopulation(ArrayList<Chromosome> population, Boolean isAsc) {
        try {
            if (isAsc) population.sort(Comparator.comparingDouble(Chromosome::getFitness).reversed());
            else population.sort(Comparator.comparingDouble(Chromosome::getFitness));
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }

     public ArrayList<Chromosome> getPopulation() {
         return newPopulation;
     }
}
