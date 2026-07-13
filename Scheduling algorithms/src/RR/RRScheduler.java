package RR;

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

class RRResult {
    List<String> executionOrder;
    List<ProcessResult> processResults;
    double averageWaitingTime;
    double averageTurnaroundTime;

    public RRResult(List<String> executionOrder, List<ProcessResult> processResults, 
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

public class RRScheduler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();
        System.out.print("Enter time quantum: ");
        int timeQuantum = scanner.nextInt();
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
            processes[i] = new Process(name, arrivalTime, burstTime);
        }

        RRResult result = executeRoundRobin(processes, timeQuantum, contextSwitchTime);
        printResults(result);
        scanner.close();
    }

    public static RRResult executeRoundRobin(Process[] processes, int timeQuantum, int contextSwitchTime) {
        Queue<Process> readyQueue = new LinkedList<>();
        List<Process> allProcesses = new ArrayList<>(Arrays.asList(processes));
        List<String> executionOrder = new ArrayList<>();
        int currentTime = 0;
        int previousIndex = -1;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        allProcesses.sort(Comparator.comparingInt(p -> p.arrivalTime));

        while (allProcesses.stream().anyMatch(p -> !p.completed)) {
            for (Process p : allProcesses) {
                if (!p.completed && p.arrivalTime <= currentTime && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }

            Process current = readyQueue.poll();
            if (previousIndex != -1 && previousIndex != allProcesses.indexOf(current)) {
                currentTime += contextSwitchTime;
            }

            int executionTime = Math.min(timeQuantum, current.remainingTime);
            executionOrder.add(current.name);
            current.remainingTime -= executionTime;
            currentTime += executionTime;

            for (Process p : allProcesses) {
                if (!p.completed && p.arrivalTime <= currentTime && !readyQueue.contains(p) && p != current) {
                    readyQueue.add(p);
                }
            }

            if (current.remainingTime == 0) {
                current.completed = true;
                current.turnaroundTime = currentTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.turnaroundTime;
            } else {
                readyQueue.add(current);
            }

            previousIndex = allProcesses.indexOf(current);
        }

        List<ProcessResult> processResults = new ArrayList<>();
        for (Process p : processes) {
            processResults.add(new ProcessResult(p.name, p.waitingTime, p.turnaroundTime));
        }

        return new RRResult(executionOrder, processResults, 
                          totalWaitingTime / processes.length, 
                          totalTurnaroundTime / processes.length);
    }

    private static void printResults(RRResult result) {
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
