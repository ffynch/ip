package beemo;

import java.io.IOException;
import java.nio.file.Path;

import beemo.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Displays Beemo's JavaFX user interface.
 */
public class Main extends Application {
    private final Beemo beemo = new Beemo(Path.of("data", "beemo.txt"));

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane mainWindow = loader.load();
        Scene scene = new Scene(mainWindow);

        stage.setScene(scene);
        stage.setTitle("Beemo");
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/DaDuke.png")));
        stage.setMinHeight(360.0);
        stage.setMinWidth(420.0);
        loader.<MainWindow>getController().setBeemo(beemo);
        stage.show();
    }
}
