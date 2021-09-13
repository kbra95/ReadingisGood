package com.example.demo.controller;

import com.example.demo.model.entity.Statistic;
import com.example.demo.service.StatisticService;
import com.example.demo.task.StatisticMetricScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/statistic")
public class StatisticController {

    private final StatisticService statisticService;
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    public ResponseEntity<List<Statistic>> getStatistics() {
        return new ResponseEntity<>(statisticService.getAllStatisticMetrics(), HttpStatus.OK);
    }
}
