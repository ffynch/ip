/**
 * Marks a task as completed.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Creates a command targeting the task at the specified zero-based index.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BeemoException {
        Task task = tasks.markAsDone(index);
        storage.saveTasks(tasks.asList());
        ui.showMarkedTask(task);
    }
}
