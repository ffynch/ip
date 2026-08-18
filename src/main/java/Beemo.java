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

        String[] tasks = new String[100];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            }
            System.out.println(divider);
        }
    }
}
