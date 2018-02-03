package pl.wozniaktomek.layout;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class WindowControl implements Initializable {
    /* Containers */
    @FXML private VBox vBox;

    /* Controls */
    @FXML private MenuItem menuClose;
    @FXML private Button buttonDefault;
    @FXML private Button buttonStart;
    @FXML private Button buttonStop;

    /* Algorithm data controls */
    @FXML private ChoiceBox<String> methodSelection;
    @FXML private ChoiceBox<String> methodCrossover;
    @FXML private ChoiceBox<String> methodMutation;
    @FXML private Spinner<Integer> probabilityCrossover;
    @FXML private Spinner<Integer> probabilityMutation;
    @FXML private TextField sizePopulation;
    @FXML private TextField sizeChromosome;
    @FXML private TextField sizeGenerations;
    @FXML private TextField rangeFrom;
    @FXML private TextField rangeTo;
    @FXML private CheckBox isChart;
    @FXML private CheckBox isChartAnimated;

    /* Chart */
    private ScatterChart<Number, Number> chart;
    private NumberAxis xAxis;
    private NumberAxis yAxis;

    /* Buttons actions methods */
    private void closeProgram() {
        Platform.exit();
        System.exit(0);
    }

    private void defaultData() {
        methodSelection.setValue("Roulette");
        methodCrossover.setValue("Single");
        methodMutation.setValue("BitString");
        probabilityCrossover.getValueFactory().setValue(50);
        probabilityMutation.getValueFactory().setValue(5);
        sizePopulation.setText("100");
        sizeChromosome.setText("16");
        sizeGenerations.setText("250");
        rangeFrom.setText("-2.048");
        rangeTo.setText("2.048");
        isChart.setSelected(true);
        isChartAnimated.setSelected(true);
    }

    /* Initialization methods */
    private void addActions() {
        menuClose.setOnAction(event -> closeProgram());
        buttonDefault.setOnAction(event -> defaultData());
    }

    private void addListeners() {
        rangeFrom.textProperty().addListener(((observable, oldValue, newValue) -> {
            newValue = newValue.replace(",", ".");
            xAxis.setLowerBound(Math.round(Double.valueOf(newValue)) - 1);
            yAxis.setLowerBound(Math.round(Double.valueOf(newValue)) - 1);
        }));

        rangeTo.textProperty().addListener(((observable, oldValue, newValue) -> {
            newValue = newValue.replace(",", ".");
            xAxis.setUpperBound(Math.round(Double.valueOf(newValue)) + 1);
            yAxis.setUpperBound(Math.round(Double.valueOf(newValue)) + 1);
        }));
    }

    private void createChart() {
        xAxis = new NumberAxis(-1, 1, 0.5);
        yAxis = new NumberAxis(-1, 1, 0.5);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillControls();
        createChart();
        addListeners();
        addActions();
    }
}
