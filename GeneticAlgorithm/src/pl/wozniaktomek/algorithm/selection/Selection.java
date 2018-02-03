package pl.wozniaktomek.algorithm.selection;

import pl.wozniaktomek.algorithm.Chromosome;
import pl.wozniaktomek.algorithm.Function;

import java.util.ArrayList;

abstract class Selection {
     ArrayList<Chromosome> oldPopulation;
     ArrayList<Chromosome> newPopulation;

    void countFitness() {
        for (Chromosome chromosome : oldPopulation)
            chromosome.setFitness(new Function().getResult(chromosome.getValueX(), chromosome.getValueY()));
    }

    void sortPopulation(Boolean isAsc) {
        if (isAsc)
            try {
                oldPopulation.sort((o1, o2) -> (int) (o2.getFitness() - o1.getFitness()));
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
            }

        else
            try {
                oldPopulation.sort((o1, o2) -> (int) (o1.getFitness() - o2.getFitness()));
            } catch (IllegalArgumentException exception) {
                exception.printStackTrace();
            }
    }

     ArrayList<Chromosome> getPopulation() {
         return newPopulation;
     }

     protected abstract void selectPopulation();

}
