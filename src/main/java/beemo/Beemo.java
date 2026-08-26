package beemo;

import java.nio.file.Path;

import beemo.command.Command;
import beemo.parser.Parser;
import beemo.storage.Storage;
import beemo.task.TaskList;
import beemo.ui.Ui;

/**
 * Coordinates Beemo's user interface, task list, command parsing, and storage.
 */
public class Beemo {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;
    private String loadingError;

    /**
     * Creates Beemo using the specified task data file.
     */
    public Beemo(Path filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (BeemoException e) {
            tasks = new TaskList();
            loadingError = e.getMessage();
        }
    }

    /**
     * Runs Beemo until the user enters the exit command or input ends.
     */
    public void run() {
        ui.showWelcome();
        if (loadingError != null) {
            ui.showError(loadingError);
            ui.showLine();
        }

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (BeemoException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Starts Beemo using the default task data file.
     */
    public static void main(String[] args) {
        new Beemo(Path.of("data", "beemo.txt")).run();
    }
}
