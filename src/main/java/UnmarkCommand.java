/**
 * Marks a task as not completed.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command targeting the task with the specified display number.
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BeemoException {
        Task task = tasks.markAsNotDone(taskNumber);
        storage.saveTasks(tasks.asList());
        ui.showUnmarkedTask(task);
    }
}
