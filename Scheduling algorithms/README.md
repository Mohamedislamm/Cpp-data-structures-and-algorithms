# CPU Scheduling Algorithms Simulator

A Java-based simulation of various CPU scheduling algorithms, complete with interactive entry points and verification test suites.

## Features
- **AG Scheduling:** A hybrid scheduling algorithm combining FCFS, Priority, and SJF phases with dynamic quantum adjustments based on the process's execution history.
- **Preemptive Shortest Job First (SJF):** Schedules processes by remaining burst time, updating dynamically on new arrivals.
- **Round Robin (RR):** Implements time-sliced scheduling with support for context switching.
- **Preemptive Priority Scheduling:** Preemptive scheduling prioritizing processes by priority rank (lower numbers indicate higher priority).
- **Interactive CLI Main Menu:** Run any of the schedulers or launch the test suites directly.

## Project Structure
- `src/`: Java packages (`AG`, `Priority`, `RR`, `SJF`) and the entry point `Main.java`.
- `testdata/`: Predefined test inputs and expected outputs in JSON format.
- `out/`: Compilation target output directory.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher.

### Compilation
Compile the project from the root folder:
```bash
javac -d out/production src/Main.java src/AG/*.java src/Priority/*.java src/RR/*.java src/SJF/*.java
```

### Execution
Run the interactive menu using the compiled classpath:
```bash
java -cp out/production Main
```
