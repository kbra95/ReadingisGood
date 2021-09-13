package com.example.demo.repository;

import com.example.demo.model.entity.Order;
import com.example.demo.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends PagingAndSortingRepository<Order,Long> {

    Page<Order> findAllByUsers(Pageable pageable,User users);

    Page<Order> findByPurchasedDateBetween(Pageable pageable, LocalDateTime startDate,LocalDateTime endDate);

    Optional<Order> findById(long id);

}
