package beemo.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import beemo.BeemoException;
import beemo.task.Deadline;
import beemo.task.Event;
import beemo.task.Task;
import beemo.task.Todo;

/**
 * Loads tasks from and saves tasks to a local data file.
 */
public class Storage {
    private static final String SEPARATOR = " | ";

    private final Path filePath;

    /**
     * Creates storage that reads from and writes to the specified file.
     *
     * @param filePath Path of the task data file.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all tasks, returning an empty list when the data file does not exist yet.
     *
     * @return Tasks loaded from the data file.
     * @throws BeemoException If the data file cannot be read or parsed.
     */
    public ArrayList<Task> loadTasks() throws BeemoException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                tasks.add(parseTask(line));
            }
            return tasks;
        } catch (IOException e) {
            throw new BeemoException(
                    "OOPS... I couldn't load your saved tasks. ╥‸╥ "
                            + "Check that the data file is readable and try again.");
        }
    }

    /**
     * Saves all tasks, creating the data directory on the first write.
     *
     * @param tasks Tasks to save.
     * @throws BeemoException If the data file cannot be written.
     */
    public void saveTasks(List<Task> tasks) throws BeemoException {
        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BeemoException(
                    "OOPS... I couldn't save your tasks. ╥‸╥ "
                            + "Check that the data folder is writable and try again.");
        }
    }

    /**
     * Converts a task into its persistent text representation.
     *
     * @param task Task to convert.
     * @return Text record representing the task.
     * @throws BeemoException If the task type is unsupported.
     */
    private String formatTask(Task task) throws BeemoException {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return String.join(SEPARATOR, "T", status, task.getDescription());
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.join(SEPARATOR, "D", status, task.getDescription(),
                    deadline.getDueDate().toString());
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return String.join(SEPARATOR, "E", status, task.getDescription(),
                    event.getStartTime(), event.getEndTime());
        }
        throw new BeemoException(
                "OOPS... I couldn't save this task. ╥‸╥ "
                        + "Use a supported todo, deadline, or event task.");
    }

    /**
     * Converts a persistent text record back into a task.
     *
     * @param line Text record to parse.
     * @return Task represented by the record.
     * @throws BeemoException If the record contains an unsupported task type.
     */
    private Task parseTask(String line) throws BeemoException {
        String[] fields = line.split(" \\| ");
        Task task;
        switch (fields[0]) {
            case "T":
                task = new Todo(fields[2]);
                break;
            case "D":
                task = new Deadline(fields[2], LocalDate.parse(fields[3]));
                break;
            case "E":
                task = new Event(fields[2], fields[3], fields[4]);
                break;
            default:
                throw new BeemoException(
                        "OOPS... I couldn't load your saved tasks. ╥‸╥ "
                                + "Check the data file for an unsupported task type.");
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
