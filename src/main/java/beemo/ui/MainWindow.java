package beemo.ui;

import beemo.Beemo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls Beemo's main chat window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image beemoImage = new Image(getClass().getResourceAsStream("/images/DaDuke.png"));
    private Beemo beemo;

    /**
     * Connects scrolling to the height of the chat history.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the Beemo instance that processes commands.
     *
     * @param beemo Beemo instance.
     */
    public void setBeemo(Beemo beemo) {
        this.beemo = beemo;
        dialogContainer.getChildren().add(DialogBox.getBeemoDialog(
                "Hello! I'm Beemo. What can I do for you?", beemoImage));
    }

    /**
     * Displays the user's command and Beemo's response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String response = beemo.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBeemoDialog(response, beemoImage));
        userInput.clear();
    }
}
