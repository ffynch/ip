/**
 * Marks a task as completed.
 */
public class MarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command targeting the task with the specified display number.
     */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BeemoException {
        Task task = tasks.markAsDone(taskNumber);
        storage.saveTasks(tasks.asList());
        ui.showMarkedTask(task);
    }
}
