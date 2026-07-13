import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n==================================================");
            System.out.println("          CPU SCHEDULING SIMULATOR MENU          ");
            System.out.println("==================================================");
            System.out.println("1. Run AG Scheduling");
            System.out.println("2. Run Preemptive Shortest Job First (SJF)");
            System.out.println("3. Run Round Robin (RR)");
            System.out.println("4. Run Preemptive Priority Scheduling");
            System.out.println("5. Run Test Suites");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    System.out.println("--- Running AG Scheduling ---");
                    AG.AGScheduler.main(new String[0]);
                    break;
                case 2:
                    System.out.println("--- Running Preemptive SJF ---");
                    SJF.SJFScheduler.main(new String[0]);
                    break;
                case 3:
                    System.out.println("--- Running Round Robin (RR) ---");
                    RR.RRScheduler.main(new String[0]);
                    break;
                case 4:
                    System.out.println("--- Running Preemptive Priority ---");
                    Priority.PriorityScheduling.main(new String[0]);
                    break;
                case 5:
                    runTestsMenu(scanner);
                    break;
                case 6:
                    System.out.println("Exiting. Thank you!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please select between 1 and 6.");
            }
        }
    }

    private static void runTestsMenu(Scanner scanner) {
        System.out.println("==============================================");
        System.out.println("               TEST RUNNER MENU               ");
        System.out.println("==============================================");
        System.out.println("1. Run AG Tests");
        System.out.println("2. Run SJF Tests");
        System.out.println("3. Run RR Tests");
        System.out.println("4. Run Priority Tests");
        System.out.println("5. Run ALL Tests");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice (1-6): ");

        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input.");
            scanner.next();
            return;
        }
        int testChoice = scanner.nextInt();
        System.out.println();

        switch (testChoice) {
            case 1:
                AG.AGTest.main(new String[0]);
                break;
            case 2:
                SJF.SJFTest.main(new String[0]);
                break;
            case 3:
                RR.RRTest.main(new String[0]);
                break;
            case 4:
                Priority.PriorityTest.main(new String[0]);
                break;
            case 5:
                System.out.println("\n>>> RUNNING AG TESTS <<<");
                AG.AGTest.main(new String[0]);
                System.out.println("\n>>> RUNNING SJF TESTS <<<");
                SJF.SJFTest.main(new String[0]);
                System.out.println("\n>>> RUNNING RR TESTS <<<");
                RR.RRTest.main(new String[0]);
                System.out.println("\n>>> RUNNING PRIORITY TESTS <<<");
                Priority.PriorityTest.main(new String[0]);
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
