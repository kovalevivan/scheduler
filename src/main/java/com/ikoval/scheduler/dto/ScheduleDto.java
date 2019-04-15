package com.ikoval.scheduler.dto;

import java.util.List;

/**
 * Data Transfer Object with represents schedule fo the specific day in response object
 * in {@link com.ikoval.scheduler.controller.SchedulerRestController}
 */

public class ScheduleDto {

    private String date;
    private List<ReservationResponseDto> schedule;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ReservationResponseDto> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ReservationResponseDto> schedule) {
        this.schedule = schedule;
    }
}
