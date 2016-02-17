package IFO;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeRoot(primaryStage);
    }

    public void initializeRoot(Stage primaryStage) {
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Intelingentý organizátor súborov");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        Temp t = new Temp();
        try {
            t.go();
        }
        catch (IOException e) {

        }
        launch(args);
    }
}
