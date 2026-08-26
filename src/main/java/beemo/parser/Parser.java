package beemo.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import beemo.BeemoException;
import beemo.command.AddCommand;
import beemo.command.Command;
import beemo.command.DeleteCommand;
import beemo.command.ExitCommand;
import beemo.command.ListCommand;
import beemo.command.MarkCommand;
import beemo.command.UnmarkCommand;
import beemo.task.Deadline;
import beemo.task.Event;
import beemo.task.Task;
import beemo.task.Todo;

/**
 * Interprets user commands and converts their arguments into application objects.
 */
public class Parser {
    private Parser() {
    }

    /**
     * Converts a line of user input into an executable command.
     */
    public static Command parse(String command) throws BeemoException {
        CommandType commandType = CommandType.from(command);
        switch (commandType) {
            case BYE:
                return new ExitCommand();
            case LIST:
                return new ListCommand();
            case MARK:
                return new MarkCommand(parseTaskNumber(command, commandType.getKeyword()));
            case UNMARK:
                return new UnmarkCommand(parseTaskNumber(command, commandType.getKeyword()));
            case DELETE:
                return new DeleteCommand(parseTaskNumber(command, commandType.getKeyword()));
            case TODO:
                // Fallthrough
            case DEADLINE:
                // Fallthrough
            case EVENT:
                return new AddCommand(parseTask(command));
            case UNKNOWN:
                // Fallthrough
            default:
                throw new BeemoException("OOPS... I don't know what that means ╥‸╥");
        }
    }

    private static int parseTaskNumber(String command, String keyword) throws BeemoException {
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
        return taskNumber;
    }

    private static Task parseTask(String command) throws BeemoException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            return parseTodo(command);
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return parseDeadline(command);
        }
        return parseEvent(command);
    }

    private static Task parseTodo(String command) throws BeemoException {
        String description = command.substring(4).trim();
        if (description.isEmpty()) {
            throw new BeemoException("OOPS... The description of a todo cannot be empty. ╥‸╥");
        }
        return new Todo(description);
    }

    private static Task parseDeadline(String command) throws BeemoException {
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

    private static Task parseEvent(String command) throws BeemoException {
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
