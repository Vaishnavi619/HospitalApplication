package com.example.demo.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SlotDTO {

	@JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public SlotDTO() {}

    public SlotDTO(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    public String getDisplay() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");
        return startTime.format(f) + " - " + endTime.format(f);
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
