import java.util.ArrayList;
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

        ArrayList<Task> tasks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            try {
                if (command.equals("bye")) {
                    System.out.println("Beemo signing off! See you next time! ૮ ˶ᵔ ᵕ ᵔ˶ ა");
                    System.out.println(divider);
                    break;
                }

                if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = getTaskIndex(command, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex));
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = getTaskIndex(command, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(taskIndex));
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    int taskIndex = getTaskIndex(command, "delete", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else if (command.equals("todo") || command.startsWith("todo ")
                        || command.equals("deadline") || command.startsWith("deadline ")
                        || command.equals("event") || command.startsWith("event ")) {
                    Task task = parseTask(command);
                    tasks.add(task);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                } else {
                    throw new BeemoException(
                            "OOPS... I don't know what that means ╥‸╥");
                }
            } catch (BeemoException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(divider);
        }
    }

    private static int getTaskIndex(String command, String keyword, int taskCount)
            throws BeemoException {
        String numberText = command.substring(keyword.length()).trim();
        if (numberText.isEmpty()) {
            throw new BeemoException(
                    "OOPS... Please provide a task number after '" + keyword + "'.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            throw new BeemoException("OOPS... '" + numberText + "' is not a valid task number. ╥‸╥");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BeemoException("OOPS... Task " + taskNumber + " is not in your list. ╥‸╥");
        }
        return taskNumber - 1;
    }

    private static Task parseTask(String command) throws BeemoException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new BeemoException("OOPS... The description of a todo cannot be empty. ╥‸╥");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String details = command.substring(8).trim();
            int byIndex = details.indexOf("/by ");
            if (byIndex < 0) {
                throw new BeemoException("OOPS... A deadline needs a '/by' date or time. ╥‸╥");
            }
            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + 4).trim();
            if (description.isEmpty()) {
                throw new BeemoException("OOPS... The description of a deadline cannot be empty. ╥‸╥");
            }
            if (by.isEmpty()) {
                throw new BeemoException("OOPS... The '/by' date or time cannot be empty. ╥‸╥");
            }
            return new Deadline(description, by);
        }

        String details = command.substring(5).trim();
        int fromIndex = details.indexOf("/from ");
        int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to ", fromIndex + 6);
        if (fromIndex < 0 || toIndex < 0) {
            throw new BeemoException("OOPS... An event needs both '/from' and '/to' times. ╥‸╥");
        }
        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + 6, toIndex).trim();
        String to = details.substring(toIndex + 4).trim();
        if (description.isEmpty()) {
            throw new BeemoException("OOPS... The description of an event cannot be empty. ╥‸╥");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BeemoException("OOPS... Event start and end times cannot be empty. ╥‸╥");
        }
        return new Event(description, from, to);
    }
}
