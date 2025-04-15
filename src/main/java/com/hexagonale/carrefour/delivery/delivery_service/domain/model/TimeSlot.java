package com.hexagonale.carrefour.delivery.delivery_service.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class TimeSlot {
    private UUID id;
    private DeliveryMethod method;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private boolean reserved;

    public TimeSlot(UUID id, DeliveryMethod method, LocalDate date, LocalTime start, LocalTime end, boolean reserved) {
        this.id = id;
        this.method = method;
        this.date = date;
        this.start = start;
        this.end = end;
        this.reserved = reserved;
    }

    // Getters & Setters
    public UUID getId() { return id; }
    public DeliveryMethod getMethod() { return method; }
    public LocalDate getDate() { return date; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public boolean isReserved() { return reserved; }

    public void reserve() {
        this.reserved = true;
    }
}
