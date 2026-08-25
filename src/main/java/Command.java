/**
 * Represents an instruction that Beemo can execute.
 */
public abstract class Command {
    /**
     * Performs the command using the application's task list, UI, and storage.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage)
            throws BeemoException;

    /**
     * Returns whether this command should stop the application.
     */
    public boolean isExit() {
        return false;
    }
}
