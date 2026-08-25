/**
 * Deletes a task from the task list.
 */
public class DeleteCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command targeting the task with the specified display number.
     */
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BeemoException {
        Task removedTask = tasks.delete(taskNumber);
        storage.saveTasks(tasks.asList());
        ui.showDeletedTask(removedTask, tasks.size());
    }
}
