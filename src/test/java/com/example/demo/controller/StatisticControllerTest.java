package com.example.demo.controller;

import com.example.demo.model.entity.Statistic;
import com.example.demo.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StatisticController.class)
class StatisticControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatisticService statisticService;
    Statistic statistic;
    @BeforeEach
    void setUp(){
        statistic = new Statistic();
        statistic.setMonth("SEPTEMBER");
        statistic.setId(1L);
        statistic.setTotalOrderCount(2);
        statistic.setTotalPurchasedAmount(12.0);
        statistic.setTotalPurchasedBook(4);
    }
    @Test
    void getAllStatisticMetricsTest() throws Exception {
        when(statisticService.getAllStatisticMetrics()).thenReturn(Collections.singletonList(statistic));
        mockMvc.perform(
                get("/statistic")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].month").value("SEPTEMBER"))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }


}
