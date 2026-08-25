import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
                CommandType commandType = CommandType.from(command);
                switch (commandType) {
                case BYE:
                    ui.showGoodbye();
                    ui.showLine();
                    return;
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case MARK:
                    int markIndex = getTaskIndex(command, commandType.getKeyword(), tasks.size());
                    tasks.get(markIndex).markAsDone();
                    storage.saveTasks(tasks);
                    ui.showMarkedTask(tasks.get(markIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = getTaskIndex(command, commandType.getKeyword(), tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    storage.saveTasks(tasks);
                    ui.showUnmarkedTask(tasks.get(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = getTaskIndex(command, commandType.getKeyword(), tasks.size());
                    Task removedTask = tasks.remove(deleteIndex);
                    storage.saveTasks(tasks);
                    ui.showDeletedTask(removedTask, tasks.size());
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    Task task = parseTask(command);
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

    private static int getTaskIndex(String command, String keyword, int taskCount)
            throws BeemoException {
        String numberText = command.substring(keyword.length()).trim();
        if (numberText.isEmpty()) {
            throw new BeemoException(
                    "OOPS... Please provide a task number after '" + keyword + "'.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new BeemoException("OOPS... '" + numberText + "' is not a valid task number. ╥‸╥");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BeemoException("OOPS... Task " + taskNumber + " is not in your list. ╥‸╥");
        }
        return taskNumber - 1;
    }

    private static Task parseTask(String command) throws BeemoException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new BeemoException("OOPS... The description of a todo cannot be empty. ╥‸╥");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String details = command.substring(8).trim();
            int byIndex = details.indexOf("/by ");
            if (byIndex < 0) {
                throw new BeemoException("OOPS... A deadline needs a '/by' date or time. ╥‸╥");
            }
            String description = details.substring(0, byIndex).trim();
            String byText = details.substring(byIndex + 4).trim();
            if (description.isEmpty()) {
                throw new BeemoException("OOPS... The description of a deadline cannot be empty. ╥‸╥");
            }
            if (byText.isEmpty()) {
                throw new BeemoException("OOPS... The '/by' date or time cannot be empty. ╥‸╥");
            }
            try {
                return new Deadline(description, LocalDate.parse(byText));
            } catch (DateTimeParseException e) {
                throw new BeemoException(
                        "OOPS... Deadline dates must use the yyyy-MM-dd format. ╥‸╥");
            }
        }

        String details = command.substring(5).trim();
        int fromIndex = details.indexOf("/from ");
        int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to ", fromIndex + 6);
        if (fromIndex < 0 || toIndex < 0) {
            throw new BeemoException("OOPS... An event needs both '/from' and '/to' times. ╥‸╥");
        }
        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + 6, toIndex).trim();
        String to = details.substring(toIndex + 4).trim();
        if (description.isEmpty()) {
            throw new BeemoException("OOPS... The description of an event cannot be empty. ╥‸╥");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BeemoException("OOPS... Event start and end times cannot be empty. ╥‸╥");
        }
        return new Event(description, from, to);
    }
}
