import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Owns the task collection and provides operations that can change it.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds a task to the end of the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task with the specified display number.
     */
    public Task delete(int taskNumber) throws BeemoException {
        return tasks.remove(toIndex(taskNumber));
    }

    /**
     * Marks and returns the task with the specified display number as done.
     */
    public Task markAsDone(int taskNumber) throws BeemoException {
        Task task = tasks.get(toIndex(taskNumber));
        task.markAsDone();
        return task;
    }

    /**
     * Marks and returns the task with the specified display number as not done.
     */
    public Task markAsNotDone(int taskNumber) throws BeemoException {
        Task task = tasks.get(toIndex(taskNumber));
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns a read-only view of the tasks for display and storage.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    private int toIndex(int taskNumber) throws BeemoException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BeemoException(
                    "OOPS... Task " + taskNumber + " is not in your list. ╥‸╥");
        }
        return taskNumber - 1;
    }
}
