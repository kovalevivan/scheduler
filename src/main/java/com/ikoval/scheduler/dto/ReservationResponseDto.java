package com.ikoval.scheduler.dto;

/**
 * Data Transfer Object with represents specific reservation entry in response object
 * in {@link com.ikoval.scheduler.controller.SchedulerRestController}
 */

public class ReservationResponseDto {

    private String startTime;
    private String endTime;
    private String employeeId;

    public ReservationResponseDto(String startTime, String endTime, String employeeId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.employeeId = employeeId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
