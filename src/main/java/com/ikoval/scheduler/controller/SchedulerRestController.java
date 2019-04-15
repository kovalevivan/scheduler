package com.ikoval.scheduler.controller;


import com.ikoval.scheduler.entity.ReservationEntry;
import com.ikoval.scheduler.dto.RequestDto;
import com.ikoval.scheduler.dto.ReservationRequestDto;
import com.ikoval.scheduler.dto.ReservationResponseDto;
import com.ikoval.scheduler.dto.ScheduleDto;
import com.ikoval.scheduler.service.SchedulerService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(value = "/api/v1")
public class SchedulerRestController {

    Logger LOG = LoggerFactory.getLogger(getClass());

    private final static DateTimeFormatter inputDateTimeFormatter
            = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private final static DateTimeFormatter inputDateTimeFormatter2
            = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

    private final static DateTimeFormatter outputDateFormatter
            = DateTimeFormat.forPattern("yyyy-MM-dd");
    private final static DateTimeFormatter outputTimeFormatter
            = DateTimeFormat.forPattern("HH:mm");

    @Autowired
    private SchedulerService service;

    /**
     * Receive requests and return prepared schedule for all saved reservations for all time
     * including reservations in this request.
     *
     * Before processing, list of submitted reservations sorted in
     * chronological way by submissionTime.
     *
     * @param requestDto
     * @return
     */

    @RequestMapping(value = "/reserve")
    public List<ScheduleDto> submitReservation(@Valid @RequestBody RequestDto requestDto) {
        List<ReservationEntry> submissionList = requestDto.getRequestList().stream()
                .map(this::buildReservationEntry)
                .sorted(Comparator.comparing(ReservationEntry::getSubmissionTime))
                .collect(Collectors.toList());
        LocalTime startOfficeHour = LocalTime.parse("09:00"/*requestDto.getOfficeHours().substring(0,4)*/);
        LocalTime endOfficeHour = LocalTime.parse("18:00"/*requestDto.getOfficeHours().substring(4)*/);
        LOG.debug("submitReservation() startOfficeHour: {} endOfficeHour: {}",
                startOfficeHour.toString(), endOfficeHour.toString());
        List<ReservationEntry> responseList = service.processInputRequest(submissionList,
                startOfficeHour,endOfficeHour);
        return buildSchedules(responseList);
    }

    /**
     * Group reservations by date and sort them by start time
     * @param reservationsList
     * @return
     */

    private List<ScheduleDto> buildSchedules(List<ReservationEntry> reservationsList) {
        Map<String, List<ReservationResponseDto>> schedulesMap = new HashMap<>();

        for(ReservationEntry entry : reservationsList) {
            LocalDate date = new LocalDate(entry.getStartReservation());
            String dateString = date.toString(outputDateFormatter);
            if(schedulesMap.containsKey(dateString)) {
                schedulesMap.get(dateString).add(buildReservationResponseDto(entry));
            } else {
                ReservationResponseDto reservationResponseDto = buildReservationResponseDto(entry);
                List<ReservationResponseDto> responseDtoList = new ArrayList<>();
                responseDtoList.add(reservationResponseDto);
                schedulesMap.put(dateString,responseDtoList);
            }
        }

        List<ScheduleDto> schedulesList = new ArrayList<>();

        for(Map.Entry<String, List<ReservationResponseDto>> entry : schedulesMap.entrySet()) {
            ScheduleDto scheduleDto = new ScheduleDto();
            scheduleDto.setDate(entry.getKey());
            scheduleDto.setSchedule(entry.getValue()
                    .stream()
                    .sorted(Comparator.comparing(ReservationResponseDto::getStartTime))
                    .collect(Collectors.toList()));
            schedulesList.add(scheduleDto);
        }

        return schedulesList.stream()
                .sorted(Comparator.comparing(ScheduleDto::getDate))
                .collect(Collectors.toList());
    }

    private ReservationResponseDto buildReservationResponseDto(ReservationEntry entry) {
        String startTime = new LocalTime(entry.getStartReservation()).toString(outputTimeFormatter);
        String endTime = new LocalTime(entry.getEndReservation()).toString(outputTimeFormatter);
        String employeeId = entry.getEmployeeId();
        return new ReservationResponseDto(startTime,endTime,employeeId);
    }


    private ReservationEntry buildReservationEntry(ReservationRequestDto reservationRequestDto) {
        DateTime submissionDateTime
                = DateTime.parse(reservationRequestDto.getSubmissionTime(), inputDateTimeFormatter);
        LOG.debug("buildReservationEntry() submissionDateTime: {} from string: {}",submissionDateTime,reservationRequestDto.getSubmissionTime());
        DateTime startDateTime
                = DateTime.parse(reservationRequestDto.getMeetingStartTime(), inputDateTimeFormatter2);
        DateTime endDateTime = startDateTime.plusHours(reservationRequestDto.getMeetingDurationHours());
        String employeeId = reservationRequestDto.getEmployeeId();
        return new ReservationEntry(submissionDateTime.toDate().getTime(),
                employeeId,startDateTime.toDate().getTime(),
                endDateTime.toDate().getTime());
    }


}
