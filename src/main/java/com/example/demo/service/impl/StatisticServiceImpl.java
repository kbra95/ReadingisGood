package com.example.demo.service.impl;

import com.example.demo.model.entity.Statistic;
import com.example.demo.repository.StatisticRepository;
import com.example.demo.service.StatisticService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    private final StatisticRepository statisticRepository;

    public StatisticServiceImpl(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @Override
    public List<Statistic> getAllStatisticMetrics() {
        return statisticRepository.findAll();
    }
}
