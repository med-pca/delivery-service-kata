package com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.dto;


import com.hexagonale.carrefour.delivery.delivery_service.domain.model.DeliveryMethod;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record TimeSlotDTO(
        UUID id,
        DeliveryMethod method,
        LocalDate date,
        LocalTime start,
        LocalTime end,
        boolean reserved
) {}
