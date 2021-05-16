package com.hotelbeds.supplierintegrations.hackertest.controller;


import com.hotelbeds.supplierintegrations.hackertest.detector.HackerDetector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hackerDetector")
@Slf4j
public class HackerDetectorControler {

    @Autowired
    private HackerDetector hackerDetector;

    @GetMapping(value = "")
    @ResponseStatus(code = HttpStatus.OK)
    public String getHackerDetector (String line) {
        return hackerDetector.parseLine(line);
    }


}
