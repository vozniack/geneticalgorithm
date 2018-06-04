package pl.wozniaktomek.layout;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;
import pl.wozniaktomek.algorithm.components.report.Report;
import pl.wozniaktomek.algorithm.components.report.ReportData;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Function Tomek Woźniak
 * @version 1.0
 */
public class WindowControl implements Initializable {
    /* Containers */
    @FXML private VBox vBox;
    @FXML private HBox tournamentBox;
    @FXML private AnchorPane reportPane;

    /* Main controls */
    @FXML private MenuItem menuClose;
    @FXML private Button buttonStart;
    @FXML private Button buttonStop;
    @FXML private Button buttonDefault;
    @FXML private Button buttonShowReport;
    @FXML private Button buttonCloseReport;
    @FXML private Button buttonSaveReport;
    @FXML private CheckBox chartActive;
    @FXML private CheckBox chartAnimated;

    /* General controls */
    @FXML private Spinner<Integer> sizePopulation;
    @FXML private Spinner<Integer> sizeChromosome;
    @FXML private Spinner<Integer> sizeGenerations;
    @FXML private Spinner<Double> rangeFrom;
    @FXML private Spinner<Double> rangeTo;

    /* Function controls */
    @FXML private ChoiceBox<String> function;
    @FXML private Text functionType;
    @FXML private Text functionExtreme;

    /* Selection controls */
    @FXML private ChoiceBox<String> methodSelection;
    @FXML private Spinner<Integer> tournamentAmount;

    /* Crossover controls */
    @FXML private ChoiceBox<String> methodCrossover;
    @FXML private Spinner<Integer> probabilityCrossover;

    /* Mutation controls */
    @FXML private ChoiceBox<String> methodMutation;
    @FXML private Spinner<Integer> probabilityMutation;

    /* Status controls */
    @FXML private Text textGeneration;
    @FXML private Text textTime;
    @FXML private Text textStatus;
    private enum ControlStatus {WORKING, UNFILLED, FINISHED}

    /* Report */
    private Report report;
    private Boolean isReport;

    /* Report console */
    @FXML private ScrollPane scrollPane;
    @FXML private TextFlow reportConsole;

    /* Chart */
    private ScatterChart<Number, Number> chart;
    private XYChart.Series<Number, Number> populationSeries;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private Double[] x;
    private Double[] y;

    /* Algorithm */
    private ExecutorService executorService;
    private GeneticAlgorithm geneticAlgorithm;
    private ArrayList<Control> controls;
    private GeneticAlgorithm.FunctionInstance functionInstance;
    private GeneticAlgorithm.FunctionSize functionSize;

    /* Timer */
    private Timer timer;
    private Long startTime;

    /** Main methods **/
    /* Algorithm */
    private void startAlgorithm() {
        chart.getData().remove(populationSeries);

        isReport = false;
        reportConsole.getChildren().clear();

        disableControls();
        createAlgorithm();
        functionInstance = geneticAlgorithm.getFunctionInstance();
        functionSize = geneticAlgorithm.getFunctionSize();

        startTime();
        executorService.submit(geneticAlgorithm);
        updateStatus(ControlStatus.WORKING);
    }

    private void createAlgorithm() {
        // Population settings
        geneticAlgorithm.createPopulation(sizePopulation.getValue(), sizeChromosome.getValue(), rangeFrom.getValue(), rangeTo.getValue());

        // Condition settings
        geneticAlgorithm.setGenerationsAmount(sizeGenerations.getValue());

        // Probabilities settings
        geneticAlgorithm.setProbabilities(probabilityCrossover.getValue(), probabilityMutation.getValue());

        // Methods settings
        GeneticAlgorithm.SelectionMethod selectionMethod = null;
        GeneticAlgorithm.CrossoverMethod crossoverMethod = null;
        GeneticAlgorithm.MutationMethod mutationMethod = null;

        if (methodSelection.getValue().equals("Roulette")) selectionMethod = GeneticAlgorithm.SelectionMethod.ROULETTE;
        if (methodSelection.getValue().equals("Tournament")) selectionMethod = GeneticAlgorithm.SelectionMethod.TOURNAMENT;

        if (methodCrossover.getValue().equals("Single")) crossoverMethod = GeneticAlgorithm.CrossoverMethod.SINGLE;
        if (methodCrossover.getValue().equals("Double")) crossoverMethod = GeneticAlgorithm.CrossoverMethod.DOUBLE;

        if (methodMutation.getValue().equals("BitString")) mutationMethod = GeneticAlgorithm.MutationMethod.BITSTRING;
        if (methodMutation.getValue().equals("FlipBit")) mutationMethod = GeneticAlgorithm.MutationMethod.FLIPBIT;

        geneticAlgorithm.setMethods(selectionMethod, crossoverMethod, mutationMethod);

        // Function settings
        GeneticAlgorithm.FunctionInstance functionInstance = null;
        GeneticAlgorithm.FunctionType functionType = null;
        GeneticAlgorithm.FunctionSize functionSize = null;

        if (function.getValue().equals("f(x,y) = 2x^2 + 2y^2 - 4")) {
            functionInstance = GeneticAlgorithm.FunctionInstance.F1;
            functionType = GeneticAlgorithm.FunctionType.MIN;
            functionSize = GeneticAlgorithm.FunctionSize.V2;
        }

        if (function.getValue().equals("f(x,y) = 5 + 3x - 4y - x^2 + xy - y^2")) {
            functionInstance = GeneticAlgorithm.FunctionInstance.F2;
            functionType = GeneticAlgorithm.FunctionType.MAX;
            functionSize = GeneticAlgorithm.FunctionSize.V2;
        }

        if (function.getValue().equals("f(x) = x^2 - 2x + 3")) {
            functionInstance = GeneticAlgorithm.FunctionInstance.F3;
            functionType = GeneticAlgorithm.FunctionType.MIN;
            functionSize = GeneticAlgorithm.FunctionSize.V1;
        }

        if (function.getValue().equals("f(x) = -x^2 + 4x - 8")) {
            functionInstance = GeneticAlgorithm.FunctionInstance.F4;
            functionType = GeneticAlgorithm.FunctionType.MAX;
            functionSize = GeneticAlgorithm.FunctionSize.V1;
        }

        geneticAlgorithm.setFunction(functionInstance, functionType, functionSize);

        // Chart settings
        geneticAlgorithm.setChart(chartActive.isSelected());

        // Additionals settings
        if (methodSelection.getValue().equals("Tournament"))
            geneticAlgorithm.setTournamentSize(tournamentAmount.getValue());
    }

    public void finishAlgorithm(ArrayList<Chromosome> population) {
        Platform.runLater(() -> showPopulation(population));
        Platform.runLater(this::showValues);
        Platform.runLater(this::stopAlgorithm);
    }

    private void stopAlgorithm() {
        stopTime();
        enableControls();
        geneticAlgorithm.setRunning(false);
        geneticAlgorithm.interrupt();
        updateStatus(ControlStatus.FINISHED);
    }

    /* Interface updating */
    private void updateStatus(ControlStatus controlStatus) {
        if (controlStatus == ControlStatus.WORKING) {
            textStatus.setText("Working");
            textStatus.setStyle("-fx-fill: rgba(5, 125, 205, 1.0)");
        }

        if (controlStatus == ControlStatus.UNFILLED) {
            textStatus.setText("Unfilled settings");
            textStatus.setStyle("-fx-fill: rgba(223, 12, 18, 1.0)");
        }

        if (controlStatus == ControlStatus.FINISHED) {
            textStatus.setText("Finished");
            textStatus.setStyle("-fx-fill: rgba(76, 187, 23, 1.0)");
        }
    }

    public void updateGeneration(Integer currentGeneration) {
        Platform.runLater(() -> textGeneration.setText(String.valueOf(currentGeneration)));
    }

    public void updatePopulation(ArrayList<Chromosome> population) {
        Platform.runLater(() -> {
            if (chartActive.isSelected())
                showPopulation(population);
        });

        Platform.runLater(() -> geneticAlgorithm.setUpdating(false));
    }

    private void showPopulation(ArrayList<Chromosome> population) {
        chart.getData().remove(populationSeries);
        readValues(population);

        populationSeries = new XYChart.Series<>();
        populationSeries.setName("Current generation");

        if (functionSize == GeneticAlgorithm.FunctionSize.V1)
            for (int i = 0; i < x.length; i++)
                populationSeries.getData().add(new XYChart.Data<>(x[i], 0));

        else
            for (int i = 0; i < x.length; i++)
                populationSeries.getData().add(new XYChart.Data<>(x[i], y[i]));

        chart.getData().add(populationSeries);
    }

    private void readValues(ArrayList<Chromosome> population) {
        x = new Double[population.size()];
        y = new Double[population.size()];

        if (functionSize == GeneticAlgorithm.FunctionSize.V1)
            for (int i = 0; i < population.size(); i++) {
                x[i] = population.get(i).getValueX();
                y[i] = 0d;
            }

        else {
            for (int i = 0; i < population.size(); i++) {
                x[i] = population.get(i).getValueX();
                y[i] = population.get(i).getValueY();
            }
        }
    }

    private void showValues() {
        for (XYChart.Series<Number, Number> series : chart.getData())
            for (XYChart.Data<Number, Number> serie : series.getData())
                Tooltip.install(serie.getNode(), new Tooltip(String.format("(x, y) = (%1.3f, %1.3f)", serie.getXValue().doubleValue(), serie.getYValue().doubleValue())));
    }

    private void enableControls() {
        for (Control control : controls)
            control.setDisable(false);
        buttonDefault.setDisable(false);
        buttonStart.setDisable(false);
        buttonStop.setDisable(true);
        buttonShowReport.setDisable(false);
        buttonSaveReport.setDisable(false);
    }

    private void disableControls() {
        for (Control control : controls)
            control.setDisable(true);
        buttonDefault.setDisable(true);
        buttonStart.setDisable(true);
        buttonStop.setDisable(false);
        buttonShowReport.setDisable(true);
        buttonSaveReport.setDisable(true);
    }

    /* Time */
    private void startTime() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                textTime.setText(String.valueOf(System.currentTimeMillis() - startTime) + " ms");
            }}, 0, 50);
    }

    private void stopTime() {
        timer.cancel();
    }

    /* Buttons actions methods */
    private void closeProgram() {
        Platform.exit();
        System.exit(0);
    }

    private void defaultData() {
        function.setValue("f(x,y) = 2x^2 + 2y^2 - 4");
        methodSelection.setValue("Roulette");
        methodCrossover.setValue("Single");
        methodMutation.setValue("BitString");
        sizePopulation.getValueFactory().setValue(100);
        sizeChromosome.getValueFactory().setValue(16);
        sizeGenerations.getValueFactory().setValue(500);
        probabilityCrossover.getValueFactory().setValue(50);
        probabilityMutation.getValueFactory().setValue(5);
        tournamentAmount.getValueFactory().setValue(8);
        rangeFrom.getValueFactory().setValue(-2d);
        rangeTo.getValueFactory().setValue(2d);
        chartActive.setSelected(true);
        chartAnimated.setSelected(true);
    }

    /* Report */
    private void createReport() {
        reportPane.setVisible(true);

        if (!isReport) {
            ReportData reportData = new ReportData();

            reportData.setSizePopulation(String.valueOf(sizePopulation.getValue()));
            reportData.setSizeChromosome(String.valueOf(sizeChromosome.getValue()));
            reportData.setSizeGenerations(String.valueOf(sizeGenerations.getValue()));
            reportData.setRangeFrom(String.valueOf(rangeFrom.getValue()));
            reportData.setRangeTo(String.valueOf(rangeTo.getValue()));

            switch (function.getValue()) {
                case "f(x,y) = 2x^2 + 2y^2 - 4":
                    reportData.setFunction("f(x,y) = 2x^2 + 2y^2 - 4");
                    reportData.setFunctionType("minimalization");
                    reportData.setFunctionExtreme("f(0, 0) = -4");
                    reportData.setFunctionResult(-4.0);
                    break;

                case "f(x,y) = 5 + 3x - 4y - x^2 + xy - y^2":
                    reportData.setFunction("f(x,y) = 5 + 3x - 4y - x^2 + xy - y^2");
                    reportData.setFunctionType("maximization");
                    reportData.setFunctionExtreme("f(2/3, -5/3) = 28/3");
                    reportData.setFunctionResult(9.333333333);
                    break;

                case "f(x) = x^2 - 2x + 3":
                    reportData.setFunction("f(x) = x^2 - 2x + 3");
                    reportData.setFunctionType("minimalization");
                    reportData.setFunctionExtreme("f(1) = 2");
                    reportData.setFunctionResult(2.0);
                    break;

                case "f(x) = -x^2 + 4x - 8":
                    reportData.setFunction("f(x) = -x^2 + 4x - 8");
                    reportData.setFunctionType("maximization");
                    reportData.setFunctionExtreme("f(2) = -4");
                    reportData.setFunctionResult(-4.0);
                    break;
            }

            reportData.setMethodSelection(methodSelection.getValue());
            if (methodSelection.getValue().equals("Tournament"))
                reportData.setTournamentAmount(String.valueOf(tournamentAmount.getValue()));
            else reportData.setTournamentAmount(null);

            reportData.setMethodCrossover(methodCrossover.getValue());
            reportData.setProbabilityCrossover(String.valueOf(probabilityCrossover.getValue()) + "%");

            reportData.setMethodMutation(methodMutation.getValue());
            reportData.setProbabilityMutation(String.valueOf(probabilityMutation.getValue()) + "%");

            reportData.setAlgorithmGenerations(textGeneration.getText());
            reportData.setAlgorithmTime(textTime.getText());
            reportData.setAlgorithmStatus(textStatus.getText());

            reportData.setPopulation(geneticAlgorithm.getCurrentPopulation());

            report = new Report(reportData, functionInstance, functionSize);
            report.createReport();
            isReport = true;
        }
    }

    public void logMessage(String message) {
        FutureTask updateUITask = new FutureTask(() -> {
            reportConsole.getChildren().add(new Text(message));
            scrollPane.setVvalue(1.0);
        }, null);
        Platform.runLater(updateUITask);
    }

    private void saveReport() {
        report.saveReport(reportConsole);
    }

    /* Initialization methods */
    private void createAlgorithmInstance() {
        geneticAlgorithm = new GeneticAlgorithm();
    }

    private void createExecutors() {
        executorService = Executors.newSingleThreadExecutor();
    }

    private void addActions() {
        menuClose.setOnAction(event -> closeProgram());
        buttonDefault.setOnAction(event -> defaultData());
        buttonStart.setOnAction(event -> startAlgorithm());
        buttonStop.setOnAction(event -> stopAlgorithm());
        buttonShowReport.setOnAction(event -> createReport());
        buttonCloseReport.setOnAction(event -> reportPane.setVisible(false));
        buttonSaveReport.setOnAction(event -> saveReport());
    }

    private void addScrolls() {
        scrollSpinner(sizePopulation);
        scrollSpinner(sizeChromosome);
        scrollSpinner(sizeGenerations);
        scrollSpinner(tournamentAmount);
        scrollSpinner(probabilityCrossover);
        scrollSpinner(probabilityMutation);
        scrollSpinner(rangeFrom);
        scrollSpinner(rangeTo);
    }

    private void scrollSpinner(Spinner spinner) {
        spinner.setOnScroll(event -> {
            if (event.getDeltaY() < 0) {
                spinner.decrement();
            } else if (event.getDeltaY() > 0) {
                spinner.increment();
            }
        });
    }

    private void addListeners() {
        // Chart listeners
        rangeFrom.valueProperty().addListener((obs, oldValue, newValue) -> {
            xAxis.setLowerBound(newValue - 1d);
            yAxis.setLowerBound(newValue - 1d);

            if (newValue > rangeTo.getValue())
                rangeFrom.getValueFactory().setValue(rangeTo.getValue() - 0.5);
        });

        rangeTo.valueProperty().addListener((obs, oldValue, newValue) -> {
            xAxis.setUpperBound(newValue + 1d);
            yAxis.setUpperBound(newValue + 1d);

            if (newValue < rangeFrom.getValue())
                rangeTo.getValueFactory().setValue(rangeFrom.getValue() + 0.5);
        });

        chartActive.selectedProperty().addListener((observable, oldValue, newValue) -> geneticAlgorithm.setChart(newValue));
        chartAnimated.selectedProperty().addListener((observable, oldValue, newValue) -> chart.setAnimated(newValue));

        // Function listeners
        function.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.equals("f(x,y) = 2x^2 + 2y^2 - 4")) {
                functionType.setText("Minimalization");
                functionExtreme.setText("f(0, 0) = -4");
                rangeFrom.getValueFactory().setValue(-1d);
                rangeTo.getValueFactory().setValue(1d);
            }

            if (newValue.equals("f(x,y) = 5 + 3x - 4y - x^2 + xy - y^2")) {
                functionType.setText("Maximization");
                functionExtreme.setText("f(2/3, -5/3) = 28/3");
                rangeFrom.getValueFactory().setValue(-2d);
                rangeTo.getValueFactory().setValue(2d);
            }

            if (newValue.equals("f(x) = x^2 - 2x + 3")) {
                functionType.setText("Minimalization");
                functionExtreme.setText("f(1) = 2");
                rangeFrom.getValueFactory().setValue(0d);
                rangeTo.getValueFactory().setValue(2d);
            }

            if (newValue.equals("f(x) = -x^2 + 4x - 8")) {
                functionType.setText("Maximization");
                functionExtreme.setText("f(2) = -4");
                rangeFrom.getValueFactory().setValue(0d);
                rangeTo.getValueFactory().setValue(3d);
            }
        });

        // Controls listeners
        sizeGenerations.valueProperty().addListener((obs, oldValue, newValue) ->
                tournamentAmount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, newValue)));

        // Tournament method listener
        methodSelection.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue.equals("Tournament"))
                tournamentBox.setVisible(true);
            else tournamentBox.setVisible(false);
        });
    }

    private void createChart() {
        xAxis = new NumberAxis(-2, 2, 0.5);
        yAxis = new NumberAxis(-2, 2, 0.5);
        chart = new ScatterChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        vBox.getChildren().add(chart);
    }

    private void fillControls() {
        function.setItems(FXCollections.observableArrayList("f(x,y) = 2x^2 + 2y^2 - 4", "f(x,y) = 5 + 3x - 4y - x^2 + xy - y^2", "f(x) = x^2 - 2x + 3", "f(x) = -x^2 + 4x - 8"));
        methodSelection.setItems(FXCollections.observableArrayList("Roulette", "Tournament"));
        methodCrossover.setItems(FXCollections.observableArrayList("Single", "Double"));
        methodMutation.setItems(FXCollections.observableArrayList("BitString", "FlipBit"));
        sizePopulation.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000));
        sizeChromosome.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31));
        sizeGenerations.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000));
        probabilityCrossover.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        probabilityMutation.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        rangeFrom.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-25d, 25d, 0, 0.5));
        rangeTo.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(-25d, 25d, 0, 0.5));
        tournamentAmount.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, sizeGenerations.getValue()));
    }

    private void grabControls() {
        controls = new ArrayList<>();
        controls.add(function);
        controls.add(methodSelection);
        controls.add(methodCrossover);
        controls.add(methodMutation);
        controls.add(probabilityCrossover);
        controls.add(probabilityMutation);
        controls.add(sizePopulation);
        controls.add(sizeChromosome);
        controls.add(sizeGenerations);
        controls.add(rangeFrom);
        controls.add(rangeTo);
        controls.add(tournamentAmount);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grabControls();
        fillControls();
        createChart();
        addListeners();
        addScrolls();
        addActions();

        createAlgorithmInstance();
        createExecutors();
        defaultData();
    }
}