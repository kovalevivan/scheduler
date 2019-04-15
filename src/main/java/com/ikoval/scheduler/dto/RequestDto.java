package com.ikoval.scheduler.dto;

import java.util.List;

/**
 * Data Transfer Object which represents request submission
 * in {@link com.ikoval.scheduler.controller.SchedulerRestController}
 */

public class RequestDto {


    private String officeHours;

    List<ReservationRequestDto> requestList;


    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public List<ReservationRequestDto> getRequestList() {
        return requestList;
    }

    public void setRequestList(List<ReservationRequestDto> requestList) {
        this.requestList = requestList;
    }
}
