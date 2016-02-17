package IFO;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Intelingentý organizátor súborov");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    void createButtons() {
        Button button = new Button("Accept");
    }


    public static void main(String[] args) {
        launch(args);
        Temp t = new Temp();
        try {
            t.go();
        }
        catch (IOException e) {

        }
    }
}
