package alpha.gui;

import alpha.TaskManager;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Starts the JavaFX window for Alpha. */
public class GuiApp extends Application {
    /** Loads the main view and connects it to a task manager. */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(GuiApp.class.getResource("/view/MainWindow.fxml"));
        VBox root = loader.load();
        MainWindow controller = loader.getController();
        controller.setTaskManager(new TaskManager());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(GuiApp.class.getResource("/css/main.css").toExternalForm());
        stage.setTitle("Alpha Task Manager");
        stage.setMinWidth(560);
        stage.setMinHeight(420);
        stage.setScene(scene);
        stage.show();
    }
}
