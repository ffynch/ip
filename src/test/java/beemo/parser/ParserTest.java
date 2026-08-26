package beemo.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import beemo.BeemoException;
import beemo.command.AddCommand;
import beemo.command.Command;
import beemo.command.DeleteCommand;
import beemo.command.ExitCommand;
import beemo.command.ListCommand;
import beemo.command.MarkCommand;
import beemo.command.UnmarkCommand;
import beemo.storage.Storage;
import beemo.task.Deadline;
import beemo.task.Event;
import beemo.task.Task;
import beemo.task.TaskList;
import beemo.task.Todo;
import beemo.ui.Ui;

class ParserTest {
    @TempDir
    private Path tempDirectory;

    @Test
    void parse_supportedCommands_correctCommandTypes() throws BeemoException {
        assertAll(
                () -> assertInstanceOf(ExitCommand.class, Parser.parse("bye")),
                () -> assertInstanceOf(ListCommand.class, Parser.parse("list")),
                () -> assertInstanceOf(MarkCommand.class, Parser.parse("mark 1")),
                () -> assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1")),
                () -> assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1")),
                () -> assertInstanceOf(AddCommand.class, Parser.parse("todo read")),
                () -> assertInstanceOf(AddCommand.class,
                        Parser.parse("deadline return /by 2026-08-30")),
                () -> assertInstanceOf(AddCommand.class,
                        Parser.parse("event meeting /from 2pm /to 4pm")));
    }

    @Test
    void parse_todoCommand_addsTodoWithDescription() throws BeemoException {
        Task task = executeAddCommand("todo read book");

        assertInstanceOf(Todo.class, task);
        assertEquals("read book", task.getDescription());
    }

    @Test
    void parse_deadlineCommand_addsDeadlineWithDate() throws BeemoException {
        Task task = executeAddCommand("deadline return book /by 2026-08-30");

        Deadline deadline = assertInstanceOf(Deadline.class, task);
        assertEquals("return book", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 8, 30), deadline.getBy());
    }

    @Test
    void parse_eventCommand_addsEventWithTimes() throws BeemoException {
        Task task = executeAddCommand("event meeting /from Monday /to Tuesday");

        Event event = assertInstanceOf(Event.class, task);
        assertEquals("meeting", event.getDescription());
        assertEquals("Monday", event.getFrom());
        assertEquals("Tuesday", event.getTo());
    }

    @Test
    void parse_invalidCommands_exceptionThrown() {
        assertAll(
                () -> assertThrows(BeemoException.class, () -> Parser.parse("blah")),
                () -> assertThrows(BeemoException.class, () -> Parser.parse("mark")),
                () -> assertThrows(BeemoException.class, () -> Parser.parse("mark two")),
                () -> assertThrows(BeemoException.class, () -> Parser.parse("todo")),
                () -> assertThrows(BeemoException.class,
                        () -> Parser.parse("deadline return book")),
                () -> assertThrows(BeemoException.class,
                        () -> Parser.parse("deadline return book /by Sunday")),
                () -> assertThrows(BeemoException.class,
                        () -> Parser.parse("event meeting /from Monday")));
    }

    private Task executeAddCommand(String input) throws BeemoException {
        Command command = Parser.parse(input);
        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempDirectory.resolve("data/tasks.txt"));
        command.execute(tasks, new Ui(), storage);
        assertEquals(1, tasks.size());
        return tasks.asList().get(0);
    }
}
