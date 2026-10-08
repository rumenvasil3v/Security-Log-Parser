# Security-Log-Parser

A tiny home-made SIEM. It reads a pile of server logs, throws out the malformed ones, puts the rest in time order, and shouts when one IP address keeps tripping warnings within a few seconds of itself.

Most of the point was to build each stage with a data structure I wrote myself rather than a library one.

## What a log line looks like

```
[1717758915] [WARN] [IP: 10.0.0.99] - Login failure: bad credentials.
```

Unix timestamp, level, source IP, message.

## The pipeline

1. **Generate.** `LogGenerator` writes 1000 mock lines to `logs.txt`: mostly `INFO`, a quarter `WARN` for slow disk reads, a tenth failed logins from `10.0.0.99`, and about 5% deliberately broken lines with a missing closing bracket.
2. **Ingest.** Each line goes into `IngestionQueue`, a linked-list queue.
3. **Verify and parse.** `LogParser` pushes every `[` onto a `CustomStack` and pops on `]`. Unbalanced brackets mean the line is flagged as a violation instead of being parsed. Lines that pass are split into a `LogRecord`.
4. **Sort.** `RadixSorter` orders the records by timestamp with a base-10 radix sort, since timestamps are plain integers.
5. **Search.** `ThreatDetector.binarySearchTimestamp` finds a record by timestamp in the sorted list. The engine uses it on the middle record as a sanity check.
6. **Detect.** `scanForBruteForce` walks the sorted list, and when a `WARN` is followed by more `WARN`s from the same IP within 5 seconds, it prints an alert.

## Running it

From inside `SecurityLogParser`, because `logs.txt` is rewritten in the current directory every run:

```
javac -d bin src/main/*.java
java -cp bin main.LogEngine
```

A run prints a timing report for each stage, the binary search check, and then the alerts, which look like this:

```
[ALERT] Dynamic Brute Force Warning! IP Address [10.0.0.99] generated 3 failed login attempts within 5 seconds.
```

## Two things that don't match the label

The generator never closes its `BufferedWriter`, so the last chunk of output never reaches disk. A run writes 777 of the 1000 lines, and the report still says "Total Logs Read from File: 1000". Adding `writer.close()` (or a try-with-resources) fixes it.

The detector counts any `WARN` from the same IP, not only login failures. The mock data is built so `10.0.0.99` is what actually trips it, but a real detector would match on the message too.
