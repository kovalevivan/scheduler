package com.ikoval.scheduler.service;

import com.ikoval.scheduler.entity.ReservationEntry;
import com.ikoval.scheduler.repository.ReservationEntryRepository;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SchedulerService {

    @Autowired
    ReservationEntryRepository repository;

    Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Process submitted reservations and return list of all saved reservations.
     *
     */

    public List<ReservationEntry> processInputRequest(List<ReservationEntry> reservationEntries, LocalTime startOfficeHour,
                                                      LocalTime endOfficeHour) {
        LOG.debug("processInputRequest() request: {}", reservationEntries);
        reservationEntries.stream()
                .filter(entry -> isReservationDuringOfficeHours(entry,startOfficeHour,endOfficeHour))
                .filter(entry -> isTimeFree(entry))
                .forEach(this::save);
        return getAllReservations();
    }

    /**
     * Checking is reservation time during office hours or not
     */

    private boolean isReservationDuringOfficeHours(ReservationEntry reservation, LocalTime startOfficeHour, LocalTime endOfficeHour) {
        LocalTime reservationStartHour = new LocalTime(reservation.getStartReservation());
        LocalTime reservationEndHour = new LocalTime(reservation.getEndReservation());

        boolean result = (reservationStartHour.isAfter(startOfficeHour) || reservationStartHour.isEqual(startOfficeHour))
                && (reservationEndHour.isBefore(endOfficeHour) || reservationEndHour.isEqual(endOfficeHour));

        LOG.debug("isReservationDuringOfficeHours() startHour: {} endHour: {} startOfficeHour: {} endOfficeHour: {} : {}",
                reservationStartHour, reservationEndHour, startOfficeHour, endOfficeHour, result);
        return result;
    }

    /**
     * Check are there any already submitted reservations that satisfied the following conditions:
     *
     *
     * 1. When start of the saved reservations greater then startReservation and less than endReservation.
     *
     *                    saved
     *           |------------------------|
     *
     *         current
     * |----------------------|
     *
     * 2. When the end of the saved reservations greater then startReservation and less than endReservation
     *
     *          saved
     * |----------------------|
     *
     *                     current
     *           |------------------------|
     *
     * 3. When the start of the saved reservation less than startReservation and greater than endReservation
     *
     *                saved
     * |------------------------------------|
     *                current
     *          |-----------------|
     *
     *
     * Notice:  Reservations, that goes one after another are not considered overlapping
     *          if the start of second reservation equals to end of the first.
     *
     *
     */

    private boolean isTimeFree(ReservationEntry reservation) {
        Long startCurrentReservation = reservation.getStartReservation();
        Long endCurrentReservation = reservation.getEndReservation();

        List<ReservationEntry> startOverlappingReservations = repository.findByStartReservationBetween(startCurrentReservation,endCurrentReservation);
        List<ReservationEntry> endOverlappingReservations = repository.findByEndReservationBetween(startCurrentReservation,endCurrentReservation);
        List<ReservationEntry> fullyOverlappingReservations = repository.findByStartReservationLessThanAndEndReservationGreaterThan(startCurrentReservation,endCurrentReservation);

        if(!startOverlappingReservations.isEmpty()) {
            startOverlappingReservations = startOverlappingReservations.stream()
                    .filter(entry -> (entry.getStartReservation() == endCurrentReservation))
                    .collect(Collectors.toList());
        }
        if(!endOverlappingReservations.isEmpty()) {
            endOverlappingReservations = endOverlappingReservations.stream()
                    .filter(entry -> !(entry.getEndReservation() == startCurrentReservation))
                    .collect(Collectors.toList());
        }
        LOG.debug("isTimeFree() startOverlapping: {} endOverlapping: {} fullyOverlapping: {}",
                startOverlappingReservations, endOverlappingReservations, fullyOverlappingReservations);
        boolean result = startOverlappingReservations.isEmpty()
                && endOverlappingReservations.isEmpty()
                && fullyOverlappingReservations.isEmpty();

        LOG.debug("isTimeFree() startReservation: {} endReservation: {} : {}",
                startCurrentReservation,endCurrentReservation,result);
        return result;
    }

    public List<ReservationEntry> getAllReservations() {
        return (List<ReservationEntry>) repository.findAll();
    }

    public void save(ReservationEntry reservationEntry) {
        repository.save(reservationEntry);
        LOG.debug("save() reservation: {}", reservationEntry);
    }
}
