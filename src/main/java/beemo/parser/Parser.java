package beemo.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import beemo.BeemoException;
import beemo.command.AddCommand;
import beemo.command.Command;
import beemo.command.DeleteCommand;
import beemo.command.ExitCommand;
import beemo.command.FindCommand;
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
    private static final String TODO_KEYWORD = "todo";
    private static final String DEADLINE_KEYWORD = "deadline";
    private static final String EVENT_KEYWORD = "event";
    private static final String BY_DELIMITER = "/by ";
    private static final String FROM_DELIMITER = "/from ";
    private static final String TO_DELIMITER = "/to ";

    /**
     * Prevents construction of this utility class.
     */
    private Parser() {
    }

    /**
     * Converts a line of user input into an executable command.
     *
     * @param command Full command entered by the user.
     * @return Executable command represented by the input.
     * @throws BeemoException If the command is unrecognized or malformed.
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
            case FIND:
                return new FindCommand(parseKeyword(command, commandType.getKeyword()));
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

    /**
     * Extracts and validates the keyword supplied to a find command.
     *
     * @param command Full command entered by the user.
     * @param commandKeyword Keyword that identifies the command.
     * @return Search keyword supplied by the user.
     * @throws BeemoException If the search keyword is missing.
     */
    private static String parseKeyword(String command, String commandKeyword)
            throws BeemoException {
        String keyword = command.substring(commandKeyword.length()).trim();
        if (keyword.isEmpty()) {
            throw new BeemoException("OOPS... Please provide a keyword after 'find'.");
        }
        return keyword;
    }

    /**
     * Extracts and validates the task number supplied to a task command.
     *
     * @param command Full command entered by the user.
     * @param keyword Keyword that precedes the task number.
     * @return Parsed task number.
     * @throws BeemoException If the task number is missing or is not an integer.
     */
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

    /**
     * Creates a task from a todo, deadline, or event command.
     *
     * @param command Full task command entered by the user.
     * @return Task represented by the command.
     * @throws BeemoException If the command contains invalid or missing task details.
     */
    private static Task parseTask(String command) throws BeemoException {
        assert command.startsWith("todo")
                || command.startsWith("deadline")
                || command.startsWith("event") : "Command must describe a task";
        if (command.equals("todo") || command.startsWith("todo ")) {
            return parseTodo(command);
        }
        if (command.equals("deadline") || command.startsWith("deadline ")) {
            return parseDeadline(command);
        }
        return parseEvent(command);
    }

    /**
     * Creates a todo from its command.
     *
     * @param command Full todo command entered by the user.
     * @return Todo represented by the command.
     * @throws BeemoException If the description is empty.
     */
    private static Task parseTodo(String command) throws BeemoException {
        String description = command.substring(TODO_KEYWORD.length()).trim();
        if (description.isEmpty()) {
            throw new BeemoException("OOPS... The description of a todo cannot be empty. ╥‸╥");
        }
        return new Todo(description);
    }

    /**
     * Creates a deadline from its command and parses its date.
     *
     * @param command Full deadline command entered by the user.
     * @return Deadline represented by the command.
     * @throws BeemoException If the description or date is missing, or the date is invalid.
     */
    private static Task parseDeadline(String command) throws BeemoException {
        String details = command.substring(DEADLINE_KEYWORD.length()).trim();
        int byIndex = details.indexOf(BY_DELIMITER);
        if (byIndex < 0) {
            throw new BeemoException("OOPS... A deadline needs a '/by' date or time. ╥‸╥");
        }
        String description = details.substring(0, byIndex).trim();
        String byText = details.substring(byIndex + BY_DELIMITER.length()).trim();
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

    /**
     * Creates an event from its command.
     *
     * @param command Full event command entered by the user.
     * @return Event represented by the command.
     * @throws BeemoException If the description, start time, or end time is missing.
     */
    private static Task parseEvent(String command) throws BeemoException {
        String details = command.substring(EVENT_KEYWORD.length()).trim();
        int fromIndex = details.indexOf(FROM_DELIMITER);
        int toIndex = fromIndex < 0
                ? -1
                : details.indexOf(TO_DELIMITER, fromIndex + FROM_DELIMITER.length());
        if (fromIndex < 0 || toIndex < 0) {
            throw new BeemoException("OOPS... An event needs both '/from' and '/to' times. ╥‸╥");
        }
        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + FROM_DELIMITER.length(), toIndex).trim();
        String to = details.substring(toIndex + TO_DELIMITER.length()).trim();
        if (description.isEmpty()) {
            throw new BeemoException("OOPS... The description of an event cannot be empty. ╥‸╥");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BeemoException("OOPS... Event start and end times cannot be empty. ╥‸╥");
        }
        return new Event(description, from, to);
    }
}
