package com.ikoval.scheduler.entity;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ReservationEntry {

    @Id
    private Long submissionTime;

    private String employeeId;

    private long startReservation;
    private long endReservation;

    public ReservationEntry() {
    }

    public ReservationEntry(long submissionTime, String employeeId, long startReservation, long endReservation) {
        this.submissionTime = submissionTime;
        this.employeeId = employeeId;
        this.startReservation = startReservation;
        this.endReservation = endReservation;
    }

    public Long getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(long submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public long getStartReservation() {
        return startReservation;
    }

    public void setStartReservation(long startReservation) {
        this.startReservation = startReservation;
    }

    public long getEndReservation() {
        return endReservation;
    }

    public void setEndReservation(long endReservation) {
        this.endReservation = endReservation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationEntry)) return false;
        ReservationEntry that = (ReservationEntry) o;
        return getStartReservation() == that.getStartReservation() &&
                getEndReservation() == that.getEndReservation() &&
                getSubmissionTime().equals(that.getSubmissionTime()) &&
                getEmployeeId().equals(that.getEmployeeId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubmissionTime(), getEmployeeId(), getStartReservation(), getEndReservation());
    }

    @Override
    public String toString() {
        return "ReservationEntry{" +
                "submissionTime=" + submissionTime +
                ", employeeId='" + employeeId + '\'' +
                ", startReservation=" + startReservation +
                ", endReservation=" + endReservation +
                '}';
    }
}
