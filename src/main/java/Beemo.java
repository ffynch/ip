import java.nio.file.Path;

public class Beemo {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(Path.of("data", "beemo.txt"));
        TaskList tasks;
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (BeemoException e) {
            tasks = new TaskList();
            ui.showError(e.getMessage());
            ui.showLine();
        }
        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showLine();

            try {
                CommandType commandType = Parser.parseCommandType(command);
                switch (commandType) {
                case BYE:
                    Command exitCommand = new ExitCommand();
                    exitCommand.execute(tasks, ui, storage);
                    if (exitCommand.isExit()) {
                        ui.showLine();
                        return;
                    }
                    break;
                case LIST:
                    Command listCommand = new ListCommand();
                    listCommand.execute(tasks, ui, storage);
                    break;
                case MARK:
                    int markIndex = Parser.parseTaskIndex(command, commandType.getKeyword(), tasks.size());
                    Task markedTask = tasks.markAsDone(markIndex);
                    storage.saveTasks(tasks.asList());
                    ui.showMarkedTask(markedTask);
                    break;
                case UNMARK:
                    int unmarkIndex = Parser.parseTaskIndex(command, commandType.getKeyword(), tasks.size());
                    Task unmarkedTask = tasks.markAsNotDone(unmarkIndex);
                    storage.saveTasks(tasks.asList());
                    ui.showUnmarkedTask(unmarkedTask);
                    break;
                case DELETE:
                    int deleteIndex = Parser.parseTaskIndex(command, commandType.getKeyword(), tasks.size());
                    Task removedTask = tasks.delete(deleteIndex);
                    storage.saveTasks(tasks.asList());
                    ui.showDeletedTask(removedTask, tasks.size());
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = Parser.parseTask(command);
                    Command addCommand = new AddCommand(task);
                    addCommand.execute(tasks, ui, storage);
                    break;
                case UNKNOWN:
                    throw new BeemoException(
                            "OOPS... I don't know what that means ╥‸╥");
                }
            } catch (BeemoException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
    }

}
