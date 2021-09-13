package com.example.demo.task;

import com.example.demo.model.PageResponse;
import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.entity.Statistic;
import com.example.demo.repository.StatisticRepository;
import com.example.demo.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StatisticMetricScheduler.class)
class StatisticMetricSchedulerTest {

    @MockBean
    private OrderService orderService;
    @MockBean
    private StatisticRepository statisticRepository;
    @Autowired
    private StatisticMetricScheduler statisticMetricScheduler;

    private final ObjectMapper mapper = new ObjectMapper();
    Statistic statistic = new Statistic();
    @BeforeEach
    void setUp(){
        statistic.setId(1L);
        statistic.setMonth(LocalDateTime.now().getMonth().toString());
        statistic.setTotalPurchasedAmount(90);
        statistic.setTotalOrderCount(3);
        statistic.setTotalPurchasedBook(7);
    }

    @SneakyThrows
    private String getContent(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("" + fileName + ".json");
        return resourceAsStream != null ? new String(resourceAsStream.readAllBytes(), StandardCharsets.UTF_8) : null;
    }
    @Test
    void updateStatisticsTest() throws IOException {
        PageResponse<OrderDTO> testData = mapper.readValue(getContent("test-data"),  new TypeReference<>() {});
        Mockito.when(orderService.getOrdersByPurchasedDate(any(), any(), any())).thenReturn(testData);
        when(statisticRepository.save(any())).thenReturn(statistic);
        Statistic returned = statisticMetricScheduler.updateStatistics();
        assertNotNull(statistic);
        assertEquals(statistic.getMonth(),returned.getMonth());
        assertEquals(statistic.getTotalOrderCount(),returned.getTotalOrderCount());
        assertEquals(statistic.getTotalPurchasedAmount(),returned.getTotalPurchasedAmount());
        assertEquals(statistic.getTotalPurchasedBook(),returned.getTotalPurchasedBook());

    }
}
