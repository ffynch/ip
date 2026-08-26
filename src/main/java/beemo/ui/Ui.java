package beemo.ui;

import java.util.List;
import java.util.Scanner;

import beemo.task.Task;

/**
 * Handles command input and text displayed to the user.
 */
public class Ui {
    private static final String DIVIDER =
            "____________________________________________________________";
    private static final String BANNER = " ____  _____ _____ __  __  ___  \n"
            + "| __ )| ____| ____|  \\/  |/ _ \\ \n"
            + "|  _ \\|  _| |  _| | |\\/| | | | |\n"
            + "| |_) | |___| |___| |  | | |_| |\n"
            + "|____/|_____|_____|_|  |_|\\___/ \n";

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates a user interface that reads commands from standard input.
     */
    public Ui() {
    }

    /**
     * Displays Beemo's greeting.
     */
    public void showWelcome() {
        showLine();
        System.out.print(BANNER);
        System.out.println("Hello! I'm Beemo.");
        System.out.println("What can I do for you?");
        showLine();
    }

    /**
     * Returns whether another command is available.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the horizontal response divider.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays Beemo's farewell.
     */
    public void showGoodbye() {
        System.out.println("Beemo signing off! See you next time! ૮ ˶ᵔ ᵕ ᵔ˶ ა");
    }

    /**
     * Displays all tasks in their current order.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was marked as done.
     */
    public void showMarkedTask(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation that a task was marked as not done.
     */
    public void showUnmarkedTask(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation that a task was deleted.
     */
    public void showDeletedTask(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Displays confirmation that a task was added.
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /**
     * Displays an error message.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    private void showTaskCount(int taskCount) {
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
