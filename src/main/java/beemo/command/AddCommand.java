package beemo.command;

import beemo.BeemoException;
import beemo.storage.Storage;
import beemo.task.Task;
import beemo.task.TaskList;
import beemo.ui.Ui;

/**
 * Adds a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that will add the supplied task.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BeemoException {
        tasks.add(task);
        storage.saveTasks(tasks.asList());
        ui.showAddedTask(task, tasks.size());
    }
}
