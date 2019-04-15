package com.ikoval.scheduler.repository;

import com.ikoval.scheduler.entity.ReservationEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationEntryRepository extends CrudRepository<ReservationEntry, Long> {

    List<ReservationEntry> findByStartReservationBetween(Long start, Long end);
    List<ReservationEntry> findByEndReservationBetween(Long start, Long end);
    List<ReservationEntry> findByStartReservationLessThanAndEndReservationGreaterThan(Long start, Long end);

}
