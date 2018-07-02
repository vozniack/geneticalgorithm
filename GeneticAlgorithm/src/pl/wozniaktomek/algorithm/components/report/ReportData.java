package pl.wozniaktomek.algorithm.components.report;

import pl.wozniaktomek.algorithm.components.Chromosome;

import java.util.ArrayList;

/**
 * @author Tomasz Woźniak
 * @version 1.0
 */
public class ReportData {
    private String sizePopulation;
    private String sizeChromosome;
    private String sizeGenerations;

    private String rangeFrom;
    private String rangeTo;

    private String function;
    private String functionType;
    private String functionExtreme;
    private Double functionResult;

    private String methodSelection;
    private String tournamentAmount;

    private String methodCrossover;
    private String probabilityCrossover;

    private String methodMutation;
    private String probabilityMutation;

    private String algorithmGenerations;
    private String algorithmTime;
    private String algorithmStatus;

    private ArrayList<Chromosome> population;

    /* Getters and setters */
    String getSizePopulation() {
        return sizePopulation;
    }

    public void setSizePopulation(String sizePopulation) {
        this.sizePopulation = sizePopulation;
    }

    String getSizeChromosome() {
        return sizeChromosome;
    }

    public void setSizeChromosome(String sizeChromosome) {
        this.sizeChromosome = sizeChromosome;
    }

    String getSizeGenerations() {
        return sizeGenerations;
    }

    public void setSizeGenerations(String sizeGenerations) {
        this.sizeGenerations = sizeGenerations;
    }

    String getRangeFrom() {
        return rangeFrom;
    }

    public void setRangeFrom(String rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    String getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(String rangeTo) {
        this.rangeTo = rangeTo;
    }

    String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    String getFunctionExtreme() {
        return functionExtreme;
    }

    public void setFunctionExtreme(String functionExtreme) {
        this.functionExtreme = functionExtreme;
    }

    Double getFunctionResult() {
        return functionResult;
    }

    public void setFunctionResult(Double functionResult) {
        this.functionResult = functionResult;
    }

    String getMethodSelection() {
        return methodSelection;
    }

    public void setMethodSelection(String methodSelection) {
        this.methodSelection = methodSelection;
    }

    String getTournamentAmount() {
        return tournamentAmount;
    }

    public void setTournamentAmount(String tournamentAmount) {
        this.tournamentAmount = tournamentAmount;
    }

     String getMethodCrossover() {
        return methodCrossover;
    }

    public void setMethodCrossover(String methodCrossover) {
        this.methodCrossover = methodCrossover;
    }

    String getProbabilityCrossover() {
        return probabilityCrossover;
    }

    public void setProbabilityCrossover(String probabilityCrossover) {
        this.probabilityCrossover = probabilityCrossover;
    }

    String getMethodMutation() {
        return methodMutation;
    }

    public void setMethodMutation(String methodMutation) {
        this.methodMutation = methodMutation;
    }

    String getProbabilityMutation() {
        return probabilityMutation;
    }

    public void setProbabilityMutation(String probabilityMutation) {
        this.probabilityMutation = probabilityMutation;
    }

    String getAlgorithmGenerations() {
        return algorithmGenerations;
    }

    public void setAlgorithmGenerations(String algorithmGenerations) {
        this.algorithmGenerations = algorithmGenerations;
    }

    String getAlgorithmTime() {
        return algorithmTime;
    }

    public void setAlgorithmTime(String algorithmTime) {
        this.algorithmTime = algorithmTime;
    }

    String getAlgorithmStatus() {
        return algorithmStatus;
    }

    public void setAlgorithmStatus(String algorithmStatus) {
        this.algorithmStatus = algorithmStatus;
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<Chromosome> population) {
        this.population = population;
    }
}
