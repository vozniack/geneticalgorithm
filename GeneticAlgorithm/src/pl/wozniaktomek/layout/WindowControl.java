package pl.wozniaktomek.layout;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import pl.wozniaktomek.algorithm.components.Function;

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
    private XYChart.Series<Number, Number> populationSeries;
    private XYChart.Series<Number, Number> minSerie;
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
        chart.getData().remove(populationSeries);
        chart.getData().remove(minSerie);

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
        // countMinimum();
    }

    public void finishAlgorithm(ArrayList<Chromosome> population) {
        Platform.runLater(() -> showPopulation(population));
        Platform.runLater(this::showValues);
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
            if (checkChart.isSelected())
                showPopulation(population);
        });

        Platform.runLater(() -> geneticAlgorithm.setUpdating(false));
    }

    private void showPopulation(ArrayList<Chromosome> population) {
        chart.getData().remove(populationSeries);
        readValues(population);

        populationSeries = new XYChart.Series<>();
        populationSeries.setName("Current generation");

        for (int i = 0; i < x.length; i++)
            populationSeries.getData().add(new XYChart.Data<>(x[i], y[i]));
        chart.getData().add(populationSeries);
    }

    private void readValues(ArrayList<Chromosome> population) {
        x = new Double[population.size()];
        y = new Double[population.size()];

        for (int i = 0; i < population.size(); i++) {
            x[i] = population.get(i).getValueX();
            y[i] = population.get(i).getValueY();
        }
    }

    private void showValues() {
        for (XYChart.Series<Number, Number> series : chart.getData())
            for (XYChart.Data<Number, Number> serie : series.getData())
                Tooltip.install(serie.getNode(), new Tooltip(String.format("(x, y) = (%1.3f, %1.3f)", serie.getXValue().doubleValue(), serie.getYValue().doubleValue())));
    }

    private void countMinimum() {
        Double from = Double.valueOf(rangeFrom.getText());
        Double to = Double.valueOf(rangeTo.getText());
        Double minimum = new Function().getResult(from, from);
        Double minX = null, minY = null;

        Double result;
        Double tmpX = from, tmpY = from;

        while (tmpX < to) {
            while (tmpY < to) {
                result = new Function().getResult(tmpX, tmpY);
                if (result < minimum) {
                    minimum = result;
                    minX = tmpX;
                    minY = tmpY;
                }

                tmpY += 0.001;
            }

            tmpY = from;
            tmpX += 0.001;
        }

        minSerie = new XYChart.Series<>();
        minSerie.setName("Minimum");
        minSerie.getData().add(new XYChart.Data<>(minX, minY));
        chart.getData().add(minSerie);
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
        methodSelection.setValue("Roulette");
        methodCrossover.setValue("Single");
        methodMutation.setValue("BitString");
        sizePopulation.setText("100");
        sizeChromosome.setText("16");
        sizeGenerations.setText("500");
        probabilityCrossover.getValueFactory().setValue(50);
        probabilityMutation.getValueFactory().setValue(5);
        rangeFrom.setText("-2");
        rangeTo.setText("2");
        checkChart.setSelected(true);
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

        checkChart.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (geneticAlgorithm != null) geneticAlgorithm.setChart(newValue);
        });
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
        chart.setLegendVisible(false);
        vBox.getChildren().add(chart);
    }

    private void fillControls() {
        methodSelection.setItems(FXCollections.observableArrayList("Roulette", "Tournament"));
        methodCrossover.setItems(FXCollections.observableArrayList("Single", "Double"));
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
