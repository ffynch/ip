/**
 * Marks a task as not completed.
 */
public class UnmarkCommand extends Command {
    private final int index;

    /**
     * Creates a command targeting the task at the specified zero-based index.
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BeemoException {
        Task task = tasks.markAsNotDone(index);
        storage.saveTasks(tasks.asList());
        ui.showUnmarkedTask(task);
    }
}
