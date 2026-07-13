package Priority;

import java.util.*;

class Process {
    String name;
    int arrivalTime, burstTime, remainingTime, priority, waitingTime, turnaroundTime;
    boolean completed;

    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completed = false;
    }
}

class PriorityResult {
    List<String> executionOrder;
    List<ProcessResult> processResults;
    double averageWaitingTime;
    double averageTurnaroundTime;

    public PriorityResult(List<String> executionOrder, List<ProcessResult> processResults,
                          double averageWaitingTime, double averageTurnaroundTime) {
        this.executionOrder = executionOrder;
        this.processResults = processResults;
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
    }
}

class ProcessResult {
    String name;
    int waitingTime;
    int turnaroundTime;

    public ProcessResult(String name, int waitingTime, int turnaroundTime) {
        this.name = name;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
    }
}

public class PriorityScheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();
        System.out.print("Enter context switching time: ");
        int contextSwitchTime = scanner.nextInt();

        Process[] processes = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.println("\nProcess " + (i + 1) + " details:");
            System.out.print("Name: ");
            String name = scanner.next();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority (lower value means higher priority): ");
            int priority = scanner.nextInt();
            processes[i] = new Process(name, arrivalTime, burstTime, priority);
        }

        PriorityResult result = executePreemptivePriority(processes, contextSwitchTime);
        printResults(result);
        scanner.close();
    }

    public static PriorityResult executePreemptivePriority(Process[] processes, int contextSwitchTime) {
        int currentTime = 0;
        int completedProcesses = 0;
        int previousIndex = -1;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        List<String> executionOrder = new ArrayList<>();

        while (completedProcesses < processes.length) {
            int highestPriorityIndex = -1;
            int highestPriority = Integer.MAX_VALUE;
            for (int i = 0; i < processes.length; i++) {
                if (!processes[i].completed && processes[i].arrivalTime <= currentTime
                        && processes[i].priority < highestPriority) {
                    highestPriority = processes[i].priority;
                    highestPriorityIndex = i;
                }
            }

            if (highestPriorityIndex == -1) {
                currentTime++;
                continue;
            }

            if (previousIndex != highestPriorityIndex) {
                if (previousIndex != -1) {
                    currentTime += contextSwitchTime;

                    int newerIndex = -1;
                    int newerPriority = Integer.MAX_VALUE;
                    for (int i = 0; i < processes.length; i++) {
                        if (!processes[i].completed && processes[i].arrivalTime <= currentTime
                                && processes[i].priority < newerPriority) {
                            newerPriority = processes[i].priority;
                            newerIndex = i;
                        }
                    }
                    if (newerIndex != -1) {
                        highestPriorityIndex = newerIndex;
                    }
                }
                executionOrder.add(processes[highestPriorityIndex].name);
            }

            Process current = processes[highestPriorityIndex];
            current.remainingTime--;
            currentTime++;

            if (current.remainingTime == 0) {
                current.completed = true;
                completedProcesses++;
                current.turnaroundTime = currentTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.turnaroundTime;
            }

            previousIndex = highestPriorityIndex;
        }

        List<ProcessResult> processResults = new ArrayList<>();
        for (Process p : processes) {
            processResults.add(new ProcessResult(p.name, p.waitingTime, p.turnaroundTime));
        }

        return new PriorityResult(executionOrder, processResults,
                totalWaitingTime / processes.length,
                totalTurnaroundTime / processes.length);
    }

    private static void printResults(PriorityResult result) {
        System.out.println("\nProcesses execution order: " + String.join(" -> ", result.executionOrder));
        System.out.println("\nWaiting Time for each process:");
        for (ProcessResult p : result.processResults) {
            System.out.println(p.name + ": " + p.waitingTime);
        }
        System.out.println("\nTurnaround Time for each process:");
        for (ProcessResult p : result.processResults) {
            System.out.println(p.name + ": " + p.turnaroundTime);
        }
        System.out.printf("\nAverage Waiting Time: %.2f\n", result.averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", result.averageTurnaroundTime);
    }
}