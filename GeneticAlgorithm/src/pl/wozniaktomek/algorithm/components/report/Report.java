package pl.wozniaktomek.algorithm.components.report;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import pl.wozniaktomek.GeneticAlgorithmApp;
import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;
import pl.wozniaktomek.algorithm.components.Function;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Tomasz Woźniak
 * @version 1.0
 */
public class Report {
    private final String separator = "#################### ";
    private final String space = "";

    private ReportData reportData;
    private GeneticAlgorithm.FunctionSize functionSize;
    private GeneticAlgorithm.FunctionInstance functionInstance;
    private ArrayList<Chromosome> population;
    private Double functionResult;

    public Report(ReportData reportData, GeneticAlgorithm.FunctionInstance functionInstance, GeneticAlgorithm.FunctionSize functionSize) {
        this.reportData = reportData;
        this.functionInstance = functionInstance;
        this.functionSize = functionSize;
        population = reportData.getPopulation();
        functionResult = reportData.getFunctionResult();
        countChromosomesValues();
    }

    public void createReport() {
        logHeader();
        logSettings();
        logResults();
    }

    public void saveReport(TextFlow reportConsole) {
        String reportContent = catchReportContent(reportConsole);

        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showSaveDialog(GeneticAlgorithmApp.stage);

        if (file != null)
            saveFile(reportContent, file);
    }

    private String catchReportContent(TextFlow reportConsole) {
        StringBuilder consoleContentBuilder = new StringBuilder();
        for (Node node : reportConsole.getChildren())
            if (node instanceof Text)
                consoleContentBuilder.append(((Text) node).getText());
        return consoleContentBuilder.toString();
    }

    private FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileTXT = new FileChooser.ExtensionFilter("text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(fileTXT);
        fileChooser.setTitle("Choose file");
        fileChooser.setInitialFileName("geneticAlgorithm_report.txt");
        return fileChooser;
    }

    private void saveFile(String reportContent, File file) {
        String[] reportContentRows = reportContent.split("\n");

        try {
            FileWriter fileWriter = new FileWriter(file);

            for (String row : reportContentRows)
                fileWriter.append(row).append("\r\n");

            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void log(String message) {
        GeneticAlgorithmApp.windowControl.logMessage(message + "\n");
    }

    private void logHeader() {
        log(separator);
        log("Genetic Algorithm / version 1.0");
        log(new SimpleDateFormat("dd.MM.yyyy / HH:mm:ss").format(new Date()));
        log(separator);
        log(space);
    }

    private void logSettings() {
        log("## SETTINGS");
        log(space);

        log("GENERAL");
        log(" # Population size: " + reportData.getSizePopulation());
        log(" # Chromosome size: " + reportData.getSizeChromosome());
        log(" # Generations size: " + reportData.getSizeGenerations());
        log(" # Range of values: < " + reportData.getRangeFrom() + ", " + reportData.getRangeTo() + " >");
        log(space);

        log("FUNCTION");
        log(" # Function: " + reportData.getFunction());
        log(" # Type: " + reportData.getFunctionType());
        log(" # Extreme: " + reportData.getFunctionExtreme());
        log(space);

        log("SELECTION");
        log(" # Method: " + reportData.getMethodSelection());
        if (reportData.getTournamentAmount() != null)
            log(" # Tournament size: " + reportData.getTournamentAmount());
        log(space);

        log("CROSSOVER");
        log(" # Method: " + reportData.getMethodCrossover());
        log(" # Probability: " + reportData.getProbabilityCrossover());
        log(space);

        log("MUTATION");
        log(" # Method: " + reportData.getMethodMutation());
        log(" # Probability: " + reportData.getProbabilityMutation());
        log(space);

        log(separator);
        log(space);
    }

    private void logResults() {
        log("## RESULTS");
        log(space);

        log("GENERAL");
        log(" # Generations: " + reportData.getAlgorithmGenerations());
        log(" # Time: " + reportData.getAlgorithmTime());
        log(" # Status: " + reportData.getAlgorithmStatus());
        log(space);

        log("POPULATION");
        log(" # Perfect individuals: " + countPerfectResults().toString());
        log(" # Approximate individuals (0.001): " + countApproximateResults(0.001));
        log(" # Approximate individuals (0.01): " + countApproximateResults(0.01));
        log(" # Approximate individuals (0.1): " + countApproximateResults(0.1));
        log(" # Approximate individuals (1): " + countApproximateResults(1.0));
    }

    private Integer countPerfectResults() {
        Integer amount = 0;

        for (Chromosome chromosome : population)
            if (chromosome.getSmallApproximation().equals(functionResult) || chromosome.getSmallApproximation().equals(-functionResult))
                amount++;

        return amount;
    }

    private Integer countApproximateResults(Double difference) {
        Integer amount = 0;

        for (Chromosome chromosome : population)
            if (chromosome.getBigApproximation() > (functionResult - difference) && chromosome.getBigApproximation() < (functionResult + difference) ||
                    chromosome.getBigApproximation() > (-functionResult - difference) && chromosome.getBigApproximation() < (-functionResult + difference))
                amount++;

        return amount;
    }

    private void countChromosomesValues() {
        for (Chromosome chromosome : population)
            if (functionSize == GeneticAlgorithm.FunctionSize.V1)
                chromosome.setFitness(new Function(functionInstance).getResult(chromosome.getValueX()));
            else
                chromosome.setFitness(new Function(functionInstance).getResult(chromosome.getValueX(), chromosome.getValueY()));

        for (Chromosome chromosome : population)
            chromosome.countApproximation();
    }
}
