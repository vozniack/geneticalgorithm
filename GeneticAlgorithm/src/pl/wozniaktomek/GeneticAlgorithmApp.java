package pl.wozniaktomek;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.wozniaktomek.layout.WindowControl;

/**
 * @author Function Tomek Woźniak
 * @version 1.0
 */
public class GeneticAlgorithmApp extends Application {
    public static WindowControl windowControl;
    public static Stage stage;
    private Parent algorithmRoot;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout/window.fxml"));
        algorithmRoot = fxmlLoader.load();
        windowControl = fxmlLoader.getController();
        setStage(primaryStage);
        stage.show();
    }

    private void setStage(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Classical Genetic Algorithm");
        stage.setScene(new Scene(algorithmRoot));
        stage.setResizable(false);

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
