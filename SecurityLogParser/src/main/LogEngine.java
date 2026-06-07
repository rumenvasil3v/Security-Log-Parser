package main;

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
	}
}
