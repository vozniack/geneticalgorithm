package pl.wozniaktomek.algorithm.mutation;

import pl.wozniaktomek.algorithm.Chromosome;

import java.util.ArrayList;

abstract class Mutation {
    ArrayList<Chromosome> population;
    Integer probability;

    protected abstract void mutateChromosome(Chromosome chromosome);
    protected abstract void modifyChromosome(Chromosome chromosome, char[] genome);

    void mutatePopulation() {
        for (Chromosome chromosome : population)
            mutateChromosome(chromosome);
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

}
