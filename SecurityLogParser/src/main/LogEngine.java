package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogEngine {

	public static void main(String[] args) {
		String logFile = "logs.txt";
		int recordsToGenerate = 1000;
		
		LogGenerator.generateMockLogFile(logFile, recordsToGenerate);
		
		IngestionQueue queue = new IngestionQueue();
		List<LogRecord> parsedRecords = new ArrayList<>();
		List<String> logViolations = new ArrayList<>();
		
		long startTime = System.nanoTime();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(logFile));
			String line;
			while ((line = reader.readLine()) != null) {
				queue.enqueue(line);
			}
		} catch (IOException e) {
			System.err.println("Error occurred while reading the log file: " + e.getMessage());
			return;
		}
		long endTime = System.nanoTime() - startTime;
		
		// Parsing the logs
		startTime = System.nanoTime();
		while (!queue.isEmpty()) {
			String rawLog = queue.dequeue();
			LogRecord record = LogParser.parse(rawLog);
			if (record != null) {
				parsedRecords.add(record);
			} else {
				logViolations.add(rawLog);
			}
		}
		long parseEndTime = System.nanoTime() - startTime;
		
		startTime = System.nanoTime();
		RadixSorter.sort(parsedRecords);
		long sortTime = System.nanoTime() - startTime;
		
		// Output analysis performance report
        System.out.println("\n==============================================");
        System.out.println("        SIEM PERFORMANCE ANALYSIS REPORT      ");
        System.out.println("==============================================");
        System.out.printf("Total Logs Read from File : %d\n", recordsToGenerate);
        System.out.printf("Successfully Parsed       : %d records\n", parsedRecords.size());
        System.out.printf("Malformed Logs Flagged    : %d violations\n", logViolations.size());
        System.out.println("----------------------------------------------");
        System.out.printf("Queue Ingestion Time      : %.3f ms\n", endTime / 1_000_000.0);
        System.out.printf("Stack Verification Time   : %.3f ms\n", parseEndTime / 1_000_000.0);
        System.out.printf("Linear Radix Sorting Time : %.3f ms\n", sortTime / 1_000_000.0);
        System.out.println("==============================================");
        
        System.out.println("\n--- Forensic Binary Search Verification ---");
        if (!parsedRecords.isEmpty()) {
            long sampleTarget = parsedRecords.get(parsedRecords.size() / 2).getTimestamp();
            System.out.println("Searching database for mid-timeline timestamp: " + sampleTarget);
            
            long searchStartTime = System.nanoTime();
            int index = ThreatDetector.binarySearchTimestamp(parsedRecords, sampleTarget);
            long searchTime = System.nanoTime() - searchStartTime;
            
            if (index != -1) {
                System.out.printf("[FOUND] Record isolated in %.4f ms at chronological position %d!\n", (searchTime / 1_000_000.0), index);
                System.out.println("Log Identity: " + parsedRecords.get(index).toString());
            }
        }

        ThreatDetector.scanForBruteForce(parsedRecords);
	}
}
