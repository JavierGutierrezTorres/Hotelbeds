package com.hotelbeds.supplierintegrations.hackertest.detector;

import java.time.LocalDateTime;

public interface HackerDetector {

    String parseLine(String line);

    Long timeCalculation (LocalDateTime startLocalDateTime, LocalDateTime endLocalDateTime);
}
