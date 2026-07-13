package SJF;

import java.util.*;

class Process {
    String name;
    int arrivalTime, burstTime, remainingTime, waitingTime, turnaroundTime;
    boolean completed;

    public Process(String name, int arrivalTime, int burstTime) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completed = false;
    }
}

class SJFResult {
    List<String> executionOrder;
    List<ProcessResult> processResults;
    double averageWaitingTime;
    double averageTurnaroundTime;

    public SJFResult(List<String> executionOrder, List<ProcessResult> processResults,
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

public class SJFScheduler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];
        for (int i = 0; i < n; i++) {
            System.out.println("\nProcess " + (i + 1) + " details:");
            System.out.print("Name: ");
            String name = scanner.next();
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            processes[i] = new Process(name, arrivalTime, burstTime);
        }

        System.out.print("\nEnter context switching time: ");
        int contextSwitchTime = scanner.nextInt();

        SJFResult result = executePreemptiveSJF(processes, contextSwitchTime);
        printResults(result);
        scanner.close();
    }

    public static SJFResult executePreemptiveSJF(Process[] processes, int contextSwitchTime) {
        int currentTime = 0;
        int completedProcesses = 0;
        int previousProcessIndex = -1;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        List<String> executionOrder = new ArrayList<>();

        while (completedProcesses < processes.length) {
            int shortestIndex = -1;
            int shortestTime = Integer.MAX_VALUE;

            // 1. Find process with shortest remaining time
            for (int i = 0; i < processes.length; i++) {
                if (!processes[i].completed && processes[i].arrivalTime <= currentTime
                        && processes[i].remainingTime < shortestTime) {
                    shortestTime = processes[i].remainingTime;
                    shortestIndex = i;
                }
            }

            // 2. If no process is ready, increment time and continue
            if (shortestIndex == -1) {
                currentTime++;
                continue;
            }

            // 3. Handle Process Switching (Context Switch + Recording Order)
            if (previousProcessIndex != shortestIndex) {
                if (previousProcessIndex != -1) {
                    currentTime += contextSwitchTime;

                    // Re-check: Did a shorter process arrive *during* the context switch?
                    // This ensures strict Preemptive accuracy.
                    int betterIndex = -1;
                    int betterTime = Integer.MAX_VALUE;
                    for (int i = 0; i < processes.length; i++) {
                        if (!processes[i].completed && processes[i].arrivalTime <= currentTime
                                && processes[i].remainingTime < betterTime) {
                            betterTime = processes[i].remainingTime;
                            betterIndex = i;
                        }
                    }
                    if (betterIndex != -1) {
                        shortestIndex = betterIndex;
                    }
                }
                // Add to execution order ONLY when the process changes
                executionOrder.add(processes[shortestIndex].name);
            }

            // 4. Execute the chosen process for 1 unit
            Process current = processes[shortestIndex];
            // REMOVED: executionOrder.add(current.name); <--- This was the bug causing duplicates
            current.remainingTime--;
            currentTime++;

            // 5. Check for completion
            if (current.remainingTime == 0) {
                current.completed = true;
                completedProcesses++;
                current.turnaroundTime = currentTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.turnaroundTime;
            }

            previousProcessIndex = shortestIndex;
        }

        List<ProcessResult> processResults = new ArrayList<>();
        for (Process p : processes) {
            processResults.add(new ProcessResult(p.name, p.waitingTime, p.turnaroundTime));
        }

        return new SJFResult(executionOrder, processResults,
                totalWaitingTime / processes.length,
                totalTurnaroundTime / processes.length);
    }

    private static void printResults(SJFResult result) {
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