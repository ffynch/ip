import java.util.Scanner;

public class Beemo {
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        String banner = " ____  _____ _____ __  __  ___  \n"
                + "| __ )| ____| ____|  \\/  |/ _ \\ \n"
                + "|  _ \\|  _| |  _| | |\\/| | | | |\n"
                + "| |_) | |___| |___| |  | | |_| |\n"
                + "|____/|_____|_____|_|  |_|\\___/ \n";

        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Hello! I'm Beemo.");
        System.out.println("What can I do for you?");
        System.out.println(divider);

        Task[] tasks = new Task[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("Beemo signing off! See you next time! ૮ ˶ᵔ ᵕ ᵔ˶ ა");
                System.out.println(divider);
                break;
            }

            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                int taskIndex = taskNumber - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[taskIndex]);
            } else {
                Task task;
                if (command.startsWith("todo ")) {
                    String description = command.substring(5);
                    task = new Todo(description);
                } else if (command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    String description = command.substring(9, byIndex);
                    String by = command.substring(byIndex + 5);
                    task = new Deadline(description, by);
                } else if (command.startsWith("event ")) {
                    int fromIndex = command.indexOf(" /from ");
                    int toIndex = command.indexOf(" /to ");
                    String description = command.substring(6, fromIndex);
                    String from = command.substring(fromIndex + 7, toIndex);
                    String to = command.substring(toIndex + 5);
                    task = new Event(description, from, to);
                } else {
                    task = new Task(command);
                }

                tasks[taskCount] = task;
                taskCount++;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + task);
                System.out.println("Now you have " + taskCount + " tasks in the list.");
            }
            System.out.println(divider);
        }
    }
}
