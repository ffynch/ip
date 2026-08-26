package beemo.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import beemo.BeemoException;

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
     *
     * @param tasks Initial tasks to copy into the list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task with the specified display number.
     *
     * @param taskNumber Display number of the task to delete.
     * @return Deleted task.
     * @throws BeemoException If the task number is outside the list.
     */
    public Task delete(int taskNumber) throws BeemoException {
        return tasks.remove(toIndex(taskNumber));
    }

    /**
     * Marks and returns the task with the specified display number as done.
     *
     * @param taskNumber Display number of the task to mark.
     * @return Marked task.
     * @throws BeemoException If the task number is outside the list.
     */
    public Task markAsDone(int taskNumber) throws BeemoException {
        Task task = tasks.get(toIndex(taskNumber));
        task.markAsDone();
        return task;
    }

    /**
     * Marks and returns the task with the specified display number as not done.
     *
     * @param taskNumber Display number of the task to unmark.
     * @return Unmarked task.
     * @throws BeemoException If the task number is outside the list.
     */
    public Task markAsNotDone(int taskNumber) throws BeemoException {
        Task task = tasks.get(toIndex(taskNumber));
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns a read-only view of the tasks for display and storage.
     *
     * @return Unmodifiable view of the tasks.
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Converts a user-facing task number into a zero-based list index.
     *
     * @param taskNumber User-facing task number.
     * @return Zero-based index of the task.
     * @throws BeemoException If the task number is outside the list.
     */
    private int toIndex(int taskNumber) throws BeemoException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new BeemoException(
                    "OOPS... Task " + taskNumber + " is not in your list. ╥‸╥");
        }
        return taskNumber - 1;
    }
}
