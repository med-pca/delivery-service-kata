package com.hexagonale.carrefour.delivery.delivery_service.domain.port;

import com.hexagonale.carrefour.delivery.delivery_service.domain.model.DeliveryMethod;
import com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.dto.TimeSlotDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DeliveryService {
    Flux<String> getAvailableMethods();
    Flux<TimeSlotDTO> getAvailableTimeSlots(DeliveryMethod method);
    Mono<TimeSlotDTO> reserveTimeSlot(UUID id);
}


