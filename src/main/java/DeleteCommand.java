/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int index;

    /**
     * Creates a command targeting the task at the specified zero-based index.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BeemoException {
        Task removedTask = tasks.delete(index);
        storage.saveTasks(tasks.asList());
        ui.showDeletedTask(removedTask, tasks.size());
    }
}
