import java.nio.file.Path;
import java.util.ArrayList;

public class Beemo {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(Path.of("data", "beemo.txt"));
        ArrayList<Task> tasks;
        try {
            tasks = storage.loadTasks();
        } catch (BeemoException e) {
            tasks = new ArrayList<>();
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
                    ui.showGoodbye();
                    ui.showLine();
                    return;
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case MARK:
                    int markIndex = Parser.parseTaskIndex(command, commandType.getKeyword(), tasks.size());
                    tasks.get(markIndex).markAsDone();
                    storage.saveTasks(tasks);
                    ui.showMarkedTask(tasks.get(markIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = Parser.parseTaskIndex(command, commandType.getKeyword(), tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    storage.saveTasks(tasks);
                    ui.showUnmarkedTask(tasks.get(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = Parser.parseTaskIndex(command, commandType.getKeyword(), tasks.size());
                    Task removedTask = tasks.remove(deleteIndex);
                    storage.saveTasks(tasks);
                    ui.showDeletedTask(removedTask, tasks.size());
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = Parser.parseTask(command);
                    tasks.add(task);
                    storage.saveTasks(tasks);
                    ui.showAddedTask(task, tasks.size());
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
