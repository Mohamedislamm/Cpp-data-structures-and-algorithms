package AG;

import java.util.*;

class Process {
    String name;
    int arrivalTime, burstTime, remainingTime, priority, waitingTime, turnaroundTime;
    int quantum;
    int quantumUsed;
    List<Integer> quantumHistory;
    boolean completed;

    public Process(String name, int arrivalTime, int burstTime, int priority, int quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.quantumUsed = 0;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completed = false;
        this.quantumHistory = new ArrayList<>();
        this.quantumHistory.add(quantum);
    }
}

class AGResult {
    List<String> executionOrder;
    List<AGProcessResult> processResults;
    double averageWaitingTime;
    double averageTurnaroundTime;

    public AGResult(List<String> executionOrder, List<AGProcessResult> processResults, 
                    double averageWaitingTime, double averageTurnaroundTime) {
        this.executionOrder = executionOrder;
        this.processResults = processResults;
        this.averageWaitingTime = averageWaitingTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
    }
}

class AGProcessResult {
    String name;
    int waitingTime;
    int turnaroundTime;
    List<Integer> quantumHistory;

    public AGProcessResult(String name, int waitingTime, int turnaroundTime, List<Integer> quantumHistory) {
        this.name = name;
        this.waitingTime = waitingTime;
        this.turnaroundTime = turnaroundTime;
        this.quantumHistory = quantumHistory;
    }
}

public class AGScheduler {
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
            System.out.print("Priority: ");
            int priority = scanner.nextInt();
            System.out.print("Quantum: ");
            int quantum = scanner.nextInt();
            processes[i] = new Process(name, arrivalTime, burstTime, priority, quantum);
        }

        AGResult result = executeAGScheduling(processes, contextSwitchTime);
        printResults(result);
        scanner.close();
    }

    public static AGResult executeAGScheduling(Process[] processes, int contextSwitchTime) {
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
                if (p.remainingTime > 0 && p.arrivalTime <= currentTime && !readyQueue.contains(p)) {
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

            int phase25 = (int) Math.ceil(current.quantum * 0.25);
            int phase50 = (int) Math.ceil(current.quantum * 0.50);
            int remainingQuantum = current.quantum - current.quantumUsed;
            String phase = "";
            int executionTime = 0;
            boolean completed = false;
            boolean preempted = false;

            if (current.quantumUsed < phase25) {
                phase = "FCFS";
                int remainingInPhase = phase25 - current.quantumUsed;
                executionTime = Math.min(remainingInPhase, current.remainingTime);
                current.quantumUsed += executionTime;
                current.remainingTime -= executionTime;
                completed = (current.remainingTime == 0);
            } else if (current.quantumUsed < phase50) {
                phase = "Priority";
                int remainingInPhase = phase50 - current.quantumUsed;
                executionTime = Math.min(remainingInPhase, current.remainingTime);
                current.quantumUsed += executionTime;
                current.remainingTime -= executionTime;
                completed = (current.remainingTime == 0);
            } else {
                phase = "SJF";
                executionTime = 1;
                Process shorterJob = findShorterJob(allProcesses, current, currentTime, readyQueue);
                if (shorterJob != null) {
                    preempted = true;
                } else {
                    executionTime = Math.min(remainingQuantum, current.remainingTime);
                }
                current.quantumUsed += executionTime;
                current.remainingTime -= executionTime;
                completed = (current.remainingTime == 0);
            }

            executionOrder.add(current.name);
            currentTime += executionTime;

            for (Process p : allProcesses) {
                if (p.remainingTime > 0 && p.arrivalTime <= currentTime && !readyQueue.contains(p) && p != current) {
                    readyQueue.add(p);
                }
            }

            if (completed) {
                current.quantum = 0;
                current.quantumHistory.add(0);
                current.completed = true;
                current.turnaroundTime = currentTime - current.arrivalTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;
                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.turnaroundTime;
            } else if (current.quantumUsed >= current.quantum) {
                current.quantum += 2;
                current.quantumHistory.add(current.quantum);
                current.quantumUsed = 0;
                readyQueue.add(current);
            } else if (preempted && phase.equals("Priority")) {
                int remaining = current.quantum - current.quantumUsed;
                current.quantum += (int) Math.ceil(remaining / 2.0);
                current.quantumHistory.add(current.quantum);
                current.quantumUsed = 0;
                readyQueue.add(current);
            } else if (preempted && phase.equals("SJF")) {
                int remaining = current.quantum - current.quantumUsed;
                current.quantum += remaining;
                current.quantumHistory.add(current.quantum);
                current.quantumUsed = 0;
                readyQueue.add(current);
            } else {
                readyQueue.add(current);
            }

            previousIndex = allProcesses.indexOf(current);
        }

        List<AGProcessResult> processResults = new ArrayList<>();
        for (Process p : processes) {
            processResults.add(new AGProcessResult(p.name, p.waitingTime, p.turnaroundTime, 
                                                   new ArrayList<>(p.quantumHistory)));
        }

        return new AGResult(executionOrder, processResults, 
                           totalWaitingTime / processes.length, 
                           totalTurnaroundTime / processes.length);
    }

    private static Process findShorterJob(List<Process> processes, Process current, int currentTime, Queue<Process> readyQueue) {
        for (Process p : processes) {
            if (p != current && p.remainingTime > 0 && p.arrivalTime <= currentTime 
                    && p.remainingTime < current.remainingTime && !readyQueue.contains(p)) {
                return p;
            }
        }
        return null;
    }

    private static void printResults(AGResult result) {
        System.out.println("\nProcesses execution order: " + String.join(" -> ", result.executionOrder));
        System.out.println("\nWaiting Time for each process:");
        for (AGProcessResult p : result.processResults) {
            System.out.println(p.name + ": " + p.waitingTime);
        }
        System.out.println("\nTurnaround Time for each process:");
        for (AGProcessResult p : result.processResults) {
            System.out.println(p.name + ": " + p.turnaroundTime);
        }
        System.out.printf("\nAverage Waiting Time: %.2f\n", result.averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", result.averageTurnaroundTime);
        System.out.println("\nQuantum time history for each process:");
        for (AGProcessResult p : result.processResults) {
            System.out.print(p.name + ": ");
            for (int i = 0; i < p.quantumHistory.size(); i++) {
                System.out.print(p.quantumHistory.get(i));
                if (i < p.quantumHistory.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        }
    }
}
