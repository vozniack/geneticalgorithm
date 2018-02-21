package pl.wozniaktomek.layout;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.wozniaktomek.algorithm.GeneticAlgorithm;
import pl.wozniaktomek.algorithm.components.Chromosome;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WindowControl implements Initializable {
    /* Containers */
    @FXML private VBox vBox;

    /* Controls */
    @FXML private MenuItem menuClose;
    @FXML private Button buttonDefault;
    @FXML private Button buttonStart;
    @FXML private Button buttonStop;
    @FXML private CheckBox checkChart;
    @FXML private CheckBox checkAnimation;

    /* Algorithm data controls */
    @FXML private ChoiceBox<String> methodSelection;
    @FXML private ChoiceBox<String> methodCrossover;
    @FXML private ChoiceBox<String> methodMutation;
    @FXML private TextField sizePopulation;
    @FXML private TextField sizeChromosome;
    @FXML private TextField sizeGenerations;
    @FXML private Spinner<Integer> probabilityCrossover;
    @FXML private Spinner<Integer> probabilityMutation;
    @FXML private TextField rangeFrom;
    @FXML private TextField rangeTo;
    @FXML private Text textGeneration;
    @FXML private Text textTime;
    private ArrayList<Control> controls;

    /* Chart */
    private ScatterChart<Number, Number> chart;
    private XYChart.Series populationSeries;
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private Double[] x;
    private Double[] y;

    /* Algorithm */
    private ExecutorService executorService;
    private GeneticAlgorithm geneticAlgorithm;

    /* Timer */
    private Timer timer;
    private Long startTime;

    /* Main methods */
    private void startAlgorithm() {
        chart.getData().removeAll();

        if (prepareAlgorithm()) {
            disableControls();
            createAlgorithm();
            executorService.submit(geneticAlgorithm);
            startTime();
        }
    }

    private Boolean prepareAlgorithm() {
        // TODO CONTROLS CHECK

        return true;
    }

    private void createAlgorithm() {
        geneticAlgorithm = new GeneticAlgorithm(Integer.valueOf(sizePopulation.getText()), Integer.valueOf(sizeChromosome.getText()), Integer.valueOf(sizeGenerations.getText()),
                probabilityCrossover.getValue(), probabilityMutation.getValue(), Double.valueOf(rangeFrom.getText()), Double.valueOf(rangeTo.getText()));

        GeneticAlgorithm.SelectionMethod selectionMethod = null;
        if (methodSelection.getValue().equals("Roulette")) selectionMethod = GeneticAlgorithm.SelectionMethod.ROULETTE;
        if (methodSelection.getValue().equals("Tournament")) selectionMethod = GeneticAlgorithm.SelectionMethod.TOURNAMENT;

        GeneticAlgorithm.CrossoverMethod crossoverMethod = null;
        if (methodCrossover.getValue().equals("Single")) crossoverMethod = GeneticAlgorithm.CrossoverMethod.SINGLE;
        if (methodCrossover.getValue().equals("Double")) crossoverMethod = GeneticAlgorithm.CrossoverMethod.DOUBLE;

        GeneticAlgorithm.MutationMethod mutationMethod = null;
        if (methodMutation.getValue().equals("BitString")) mutationMethod = GeneticAlgorithm.MutationMethod.BITSTRING;
        if (methodMutation.getValue().equals("FlipBit")) mutationMethod = GeneticAlgorithm.MutationMethod.FLIPBIT;

        geneticAlgorithm.setMethods(selectionMethod, crossoverMethod, mutationMethod);
        geneticAlgorithm.setChart(checkChart.isSelected());
    }

    public void finishAlgorithm() {
        Platform.runLater(this::stopAlgorithm);
    }

    private void stopAlgorithm() {
        enableControls();
        geneticAlgorithm.setRunning(false);
        geneticAlgorithm.interrupt();
        stopTime();
    }

    /* Interface updating */
    public void updateGeneration(Integer currentGeneration) {
        Platform.runLater(() -> textGeneration.setText(String.valueOf(currentGeneration)));
    }

    public void updatePopulation(ArrayList<Chromosome> population) {
        Platform.runLater(() -> {
            if (checkChart.isSelected()) {
                chart.setAnimated(checkAnimation.isSelected());
                readValues(population);

                chart.getData().remove(populationSeries);
                populationSeries = new XYChart.Series();
                populationSeries.setName("Current generation");

                for (int i = 0; i < x.length; i++)
                    populationSeries.getData().add(new XYChart.Data(x[i], y[i]));
                chart.getData().add(populationSeries);
            }

            geneticAlgorithm.setUpdating(false);
        });
    }

    private void readValues(ArrayList<Chromosome> population) {
        x = new Double[population.size()];
        y = new Double[population.size()];

        for (int i = 0; i < population.size(); i++) {
            x[i] = population.get(i).getValueX();
            y[i] = population.get(i).getValueY();
        }
    }

    private void enableControls() {
        for (Control control : controls)
            control.setDisable(false);
        buttonDefault.setDisable(false);
        buttonStart.setDisable(false);
        buttonStop.setDisable(true);
    }

    private void disableControls() {
        for (Control control : controls)
            control.setDisable(true);
        buttonDefault.setDisable(true);
        buttonStart.setDisable(true);
        buttonStop.setDisable(false);
    }

    /* Time */
    private void startTime() {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                textTime.setText(String.valueOf(System.currentTimeMillis() - startTime) + " ms");
            }}, 0, 25);
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
        methodSelection.setValue("Roulette");
        methodCrossover.setValue("Single");
        methodMutation.setValue("BitString");
        sizePopulation.setText("100");
        sizeChromosome.setText("12");
        sizeGenerations.setText("250");
        probabilityCrossover.getValueFactory().setValue(50);
        probabilityMutation.getValueFactory().setValue(5);
        rangeFrom.setText("-2");
        rangeTo.setText("2");
        checkChart.setSelected(true);
        checkAnimation.setSelected(true);
    }

    /* Initialization methods */
    private void createExecutors() {
        executorService = Executors.newSingleThreadExecutor();
    }

    private void addActions() {
        menuClose.setOnAction(event -> closeProgram());
        buttonDefault.setOnAction(event -> defaultData());
        buttonStart.setOnAction(event -> startAlgorithm());
        buttonStop.setOnAction(event -> stopAlgorithm());
    }

    private void addListeners() {
        rangeFrom.textProperty().addListener(((observable, oldValue, newValue) -> {
            newValue = newValue.replace(",", ".");
            xAxis.setLowerBound(Math.round(Double.valueOf(newValue)) - 0.5);
            yAxis.setLowerBound(Math.round(Double.valueOf(newValue)) - 0.5);
        }));

        rangeTo.textProperty().addListener(((observable, oldValue, newValue) -> {
            newValue = newValue.replace(",", ".");
            xAxis.setUpperBound(Math.round(Double.valueOf(newValue)) + 0.5);
            yAxis.setUpperBound(Math.round(Double.valueOf(newValue)) + 0.5);
        }));
    }

    private void createChart() {
        xAxis = new NumberAxis(-3, 3, 0.5);
        yAxis = new NumberAxis(-3, 3, 0.5);
        chart = new ScatterChart<>(xAxis, yAxis);
        vBox.getChildren().add(chart);
    }

    private void fillControls() {
        methodSelection.setItems(FXCollections.observableArrayList("Roulette", "Tournament"));
        methodCrossover.setItems(FXCollections.observableArrayList("Single", "Double", "Multi"));
        methodMutation.setItems(FXCollections.observableArrayList("BitString", "FlipBit"));
        probabilityCrossover.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
        probabilityMutation.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100));
    }

    private void grabControls() {
        controls = new ArrayList<>();
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grabControls();
        fillControls();
        createChart();
        addListeners();
        addActions();

        createExecutors();
    }
}
