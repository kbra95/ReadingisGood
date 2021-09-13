package com.example.demo.task;

import com.example.demo.model.PageResponse;
import com.example.demo.model.dto.OrderDTO;
import com.example.demo.model.dto.OrderItemDTO;
import com.example.demo.model.entity.Statistic;
import com.example.demo.repository.StatisticRepository;
import com.example.demo.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatisticMetricScheduler {

    private final OrderService orderService;
    private final StatisticRepository statisticRepository;

    public StatisticMetricScheduler(OrderService orderService, StatisticRepository statisticRepository) {
        this.orderService = orderService;
        this.statisticRepository = statisticRepository;
    }

    @Scheduled(cron = "0 24 L * ?")
    @Transactional
    public Statistic updateStatistics() {
        Statistic statistic = new Statistic();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = LocalDateTime.now().minusMonths(1).format(formatter);
        String endDate = LocalDateTime.now().format(formatter);
        PageResponse<OrderDTO> ordersByPurchasedDate = orderService
                .getOrdersByPurchasedDate(Pageable.unpaged(), startDate, endDate);
        Integer totalPurchasedBook = ordersByPurchasedDate
                .getContent().stream().map(orderDTO -> orderDTO.getOrderItems().stream()
                        .map(OrderItemDTO::getQuantity).reduce(0, Integer::sum)).reduce(0, Integer::sum);
        Double totalPrice = ordersByPurchasedDate.getContent().stream().map(OrderDTO::getTotalPrice).reduce(0.0, Double::sum);

        statistic.setMonth(LocalDateTime.now().getMonth().toString());
        statistic.setTotalOrderCount(ordersByPurchasedDate.getTotalElements());
        statistic.setTotalPurchasedBook(totalPurchasedBook);
        statistic.setTotalPurchasedAmount(totalPrice);

        statisticRepository.save(statistic);
        return statistic;
    }
}
