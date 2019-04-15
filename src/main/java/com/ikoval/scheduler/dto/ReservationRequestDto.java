package com.ikoval.scheduler.dto;

/**
 * Data Transfer object with represents specific reservation in request object
 * in {@link com.ikoval.scheduler.controller.SchedulerRestController}
 */

public class ReservationRequestDto {

    private String submissionTime;
    private String employeeId;
    private String meetingStartTime;
    private int meetingDurationHours;

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMeetingStartTime() {
        return meetingStartTime;
    }

    public void setMeetingStartTime(String meetingStartTime) {
        this.meetingStartTime = meetingStartTime;
    }

    public int getMeetingDurationHours() {
        return meetingDurationHours;
    }

    public void setMeetingDurationHours(int meetingDurationHours) {
        this.meetingDurationHours = meetingDurationHours;
    }
}
