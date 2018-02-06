package pl.wozniaktomek.algorithm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GeneticTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(100, 16, 500, 50, 5, -2.048d, 2.048d);
        geneticAlgorithm.setMethods(GeneticAlgorithm.SelectionMethod.ROULETTE, GeneticAlgorithm.CrossoverMethod.SINGLE, GeneticAlgorithm.MutationMethod.BITSTRING);

        executorService.execute(geneticAlgorithm);
        executorService.shutdown();
    }
}
