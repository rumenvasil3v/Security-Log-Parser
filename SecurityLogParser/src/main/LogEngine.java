package main;

import java.util.ArrayList;
import java.util.List;

public class LogEngine {

	public static void main(String[] args) {
		IngestionQueue queue = new IngestionQueue();

		String[] mockLogs = { "[1717758000] [INFO] [IP: 192.168.1.10] - User logged in successfully.",
				"[1717758002] [WARN] [IP: 10.0.0.5] - Invalid password attempt.",
				"[1717758005] [ERROR] [IP: 192.168.1.50 - Malformed bracket error here!",
				"[1717758010] [INFO] [IP: 192.168.1.10] - User viewing dashboard." };
		
		System.out.println("Ingesting raw logs");
		
		for (int i = 0; i < mockLogs.length; i++) {
			queue.enqueue(mockLogs[i]);
		}
		
		System.out.println("Size of queue after ingestion -> " + queue.getSize());
		
		System.out.println("\n--- Processing and Parsing Ingestion Buffer ---");
		
		List<LogRecord> parsedRecords = new ArrayList<>();
		List<String> logViolations = new ArrayList<>();
		
		while (!queue.isEmpty()) {
			String rawLog = queue.dequeue();
			LogRecord record = LogParser.parse(rawLog);
			
			if (record != null) {
				parsedRecords.add(record);
			} else {
				logViolations.add(rawLog);
			}
		}
		
		System.out.println("\nSuccessfully Parsed Records:");
		for (LogRecord record: parsedRecords) {
			System.out.println(" -> " + record.toString());
		}
		
		System.out.println("\n[ALERT] Defective or Malformed Logs Detected:");
		for (String badLog: logViolations) {
			System.out.println(" -> " + badLog);
		}
	}
}
