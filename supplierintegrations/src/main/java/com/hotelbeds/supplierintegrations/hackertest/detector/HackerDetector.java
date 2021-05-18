package com.hotelbeds.supplierintegrations.hackertest.detector;

public interface HackerDetector {

    String parseLine(String line);

    Long timeCalculation (String startDateString, String endDateString);
}
