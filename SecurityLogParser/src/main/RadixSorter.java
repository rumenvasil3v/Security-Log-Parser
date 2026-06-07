package main;

import java.util.List;

public class RadixSorter {
	
	public static void sort(List<LogRecord> records) {
		if (records == null || records.size() <= 1) {
			return;
		}
		
		long maxTimestamp = records.get(0).getTimestamp();
		for (int i = 1; i < records.size(); i++) {
			if (records.get(i).getTimestamp() > maxTimestamp) {
				maxTimestamp = records.get(i).getTimestamp();
			}
		}
		
		for (long exp = 1; maxTimestamp / exp > 0; exp *= 10) {
			countingSortByDigit(records, exp);
		}
	}
	
	private static void countingSortByDigit(List<LogRecord> records, long exp) {
		int numberOfRecords = records.size();
		LogRecord[] output = new LogRecord[numberOfRecords];
		int[] count = new int[10];
		
		for (int i = 0; i < numberOfRecords; i++) {
			int digit = (int) ((records.get(i).getTimestamp() / exp) % 10);
			count[digit]++;
		}
		
		for (int i = 1; i < 10; i++) {
			count[i] += count[i - 1];
		}
		
		for (int i = numberOfRecords - 1; i >= 0; i--) {
			int digit = (int) ((records.get(i).getTimestamp() / exp) % 10);
			output[count[digit] - 1] = records.get(i);
			count[digit]--;
		}
		
		for (int i = 0; i < numberOfRecords; i++) {
			records.set(i, output[i]);
		}
	}
}
