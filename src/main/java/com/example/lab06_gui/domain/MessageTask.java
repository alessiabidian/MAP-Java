package com.example.lab06_gui.domain;
import com.example.lab06_gui.utils.Constants;

import java.time.LocalDateTime;

public class MessageTask extends Task {

    private String message;
    private Long from;
    private Long to;
    private LocalDateTime date;

    public MessageTask(Long taskID, String description, String message, Long from, Long to, LocalDateTime date) {
        super(taskID, description);
        this.message = message;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    @Override
    public void execute() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return super.toString() + " " + message + " " + from + " " + date.format(Constants.DATE_TIME_FORMATTER);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
