package SJF;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SJFTest {
    public static void main(String[] args) {
        File dir = new File("testdata/Other_Schedulers");
        if (!dir.exists()) {
            System.out.println("Test directory not found");
            return;
        }

        File[] testFiles = dir.listFiles((d, name) -> name.startsWith("test_") && name.endsWith(".json"));
        if (testFiles == null || testFiles.length == 0) {
            System.out.println("No test files found");
            return;
        }

        Arrays.sort(testFiles);
        for (File testFile : testFiles) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("Running test: " + testFile.getName());
            System.out.println("=".repeat(60));
            try {
                runTest(testFile);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static void runTest(File testFile) throws Exception {
        String content = new String(Files.readAllBytes(testFile.toPath()));
        TestData testData = parseJSON(content);

        if (testData.processes.isEmpty()) {
            System.out.println("ERROR: No processes found");
            return;
        }

        SJF.Process[] processes = new SJF.Process[testData.processes.size()];
        for (int i = 0; i < testData.processes.size(); i++) {
            ProcessData p = testData.processes.get(i);
            processes[i] = new SJF.Process(p.name, p.arrival, p.burst);
        }

        SJF.SJFResult result = SJFScheduler.executePreemptiveSJF(processes, testData.contextSwitch);
        if (result == null || result.executionOrder == null || result.processResults == null) {
            System.out.println("ERROR: Invalid result");
            return;
        }
        printResults(result);

        ExpectedOutput expected = testData.expectedOutput;
        boolean passed = true;

        if (!result.executionOrder.equals(expected.SJF.executionOrder)) {
            System.out.println("FAILED: Execution order mismatch");
            passed = false;
        }

        Map<String, SJF.ProcessResult> resultMap = new HashMap<>();
        for (SJF.ProcessResult pr : result.processResults) {
            resultMap.put(pr.name, pr);
        }

        for (ProcessResult expectedPr : expected.SJF.processResults) {
            SJF.ProcessResult actualPr = resultMap.get(expectedPr.name);
            if (actualPr == null || actualPr.waitingTime != expectedPr.waitingTime
                    || actualPr.turnaroundTime != expectedPr.turnaroundTime) {
                passed = false;
            }
        }

        if (Math.abs(result.averageWaitingTime - expected.SJF.averageWaitingTime) > 0.01 ||
            Math.abs(result.averageTurnaroundTime - expected.SJF.averageTurnaroundTime) > 0.01) {
            passed = false;
        }

        System.out.println(passed ? "matched expected output" : "FAILED");
    }

    private static void printResults(SJF.SJFResult result) {
        System.out.println("\nActual Results:");
        System.out.println("Execution order: " + String.join(" -> ", result.executionOrder));
        System.out.println("\nWaiting Time for each process:");
        for (SJF.ProcessResult p : result.processResults) {
            System.out.println("  " + p.name + ": " + p.waitingTime);
        }
        System.out.println("\nTurnaround Time for each process:");
        for (SJF.ProcessResult p : result.processResults) {
            System.out.println("  " + p.name + ": " + p.turnaroundTime);
        }
        System.out.printf("\nAverage Waiting Time: %.2f\n", result.averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", result.averageTurnaroundTime);
    }

    private static TestData parseJSON(String json) {
        TestData data = new TestData();
        int inputStart = json.indexOf("\"input\":");
        if (inputStart == -1) inputStart = 0;

        data.contextSwitch = extractInt(json, inputStart, "contextSwitch");

        int procKeyStart = json.indexOf("\"processes\"", inputStart);
        if (procKeyStart != -1) {
            int procStart = json.indexOf("[", procKeyStart);
            if (procStart != -1) {
                int procEnd = json.indexOf("]", procStart);
                if (procEnd != -1) {
                    String procArray = json.substring(procStart + 1, procEnd);
                    if (!procArray.trim().isEmpty()) {
                        String[] procs = procArray.split("\\},\\s*\\{");
                        for (String proc : procs) {
                            proc = proc.replace("{", "").replace("}", "").trim();
                            if (!proc.isEmpty()) {
                                ProcessData p = new ProcessData();
                                p.name = extractString(proc, "name");
                                p.arrival = extractInt(proc, "arrival");
                                p.burst = extractInt(proc, "burst");
                                p.priority = extractInt(proc, "priority");
                                data.processes.add(p);
                            }
                        }
                    }
                }
            }
        }

        int expStart = json.indexOf("\"expectedOutput\":");
        if (expStart != -1) {
            data.expectedOutput = new ExpectedOutput();
            data.expectedOutput.SJF = new SJFExpected();

            int sjfStart = json.indexOf("\"SJF\":", expStart);
            if (sjfStart != -1) {
                int execOrderKey = json.indexOf("\"executionOrder\"", sjfStart);
                if (execOrderKey != -1) {
                    int execOrderStart = json.indexOf("[", execOrderKey);
                    if (execOrderStart != -1) {
                        int execOrderEnd = json.indexOf("]", execOrderStart);
                        if (execOrderEnd != -1) {
                            String orderStr = json.substring(execOrderStart + 1, execOrderEnd);
                            if (!orderStr.trim().isEmpty()) {
                                String[] orders = orderStr.split(",\\s*");
                                for (String o : orders) {
                                    o = o.trim().replace("\"", "");
                                    if (!o.isEmpty()) {
                                        data.expectedOutput.SJF.executionOrder.add(o);
                                    }
                                }
                            }
                        }
                    }
                }

                int resultsKey = json.indexOf("\"processResults\"", sjfStart);
                if (resultsKey != -1) {
                    int resultsStart = json.indexOf("[", resultsKey);
                    if (resultsStart != -1) {
                        int resultsEnd = json.indexOf("]", resultsStart);
                        if (resultsEnd != -1) {
                            String resultsStr = json.substring(resultsStart + 1, resultsEnd);
                            if (!resultsStr.trim().isEmpty()) {
                                String[] results = resultsStr.split("\\},\\s*\\{");
                                for (String r : results) {
                                    r = r.replace("{", "").replace("}", "").trim();
                                    if (!r.isEmpty()) {
                                        ProcessResult pr = new ProcessResult();
                                        pr.name = extractString(r, "name");
                                        pr.waitingTime = extractInt(r, "waitingTime");
                                        pr.turnaroundTime = extractInt(r, "turnaroundTime");
                                        data.expectedOutput.SJF.processResults.add(pr);
                                    }
                                }
                            }
                        }
                    }
                }

                data.expectedOutput.SJF.averageWaitingTime = extractDouble(json, sjfStart, "averageWaitingTime");
                data.expectedOutput.SJF.averageTurnaroundTime = extractDouble(json, sjfStart, "averageTurnaroundTime");
            }
        }

        return data;
    }

    private static String extractString(String json, String key) {
        int index = json.indexOf("\"" + key + "\":");
        if (index == -1) return "";
        int start = json.indexOf(":", index) + 1;
        while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '\"')) start++;
        int end = start;
        while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '}' && json.charAt(end) != '\"') end++;
        return json.substring(start, end).replace("\"", "").trim();
    }

    private static int extractInt(String json, int startPos, String key) {
        int index = json.indexOf("\"" + key + "\":", startPos);
        if (index == -1) return 0;
        int start = json.indexOf(":", index) + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        if (end == start) return 0;
        return Integer.parseInt(json.substring(start, end).trim());
    }

    private static int extractInt(String json, String key) {
        return extractInt(json, 0, key);
    }

    private static double extractDouble(String json, int startPos, String key) {
        int index = json.indexOf("\"" + key + "\":", startPos);
        if (index == -1) return 0;
        int start = json.indexOf(":", index) + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.')) end++;
        if (end == start) return 0;
        return Double.parseDouble(json.substring(start, end).trim());
    }

    static class TestData {
        int contextSwitch = 1;
        List<ProcessData> processes = new ArrayList<>();
        ExpectedOutput expectedOutput;
    }

    static class ProcessData {
        String name;
        int arrival, burst, priority;
    }

    static class ExpectedOutput {
        SJFExpected SJF;
    }

    static class SJFExpected {
        List<String> executionOrder = new ArrayList<>();
        List<ProcessResult> processResults = new ArrayList<>();
        double averageWaitingTime, averageTurnaroundTime;
    }

    static class ProcessResult {
        String name;
        int waitingTime, turnaroundTime;
    }
}
