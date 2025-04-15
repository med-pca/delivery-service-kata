package com.hexagonale.carrefour.delivery.delivery_service.domain.port;

import com.hexagonale.carrefour.delivery.delivery_service.domain.model.DeliveryMethod;
import com.hexagonale.carrefour.delivery.delivery_service.domain.model.TimeSlot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TimeSlotRepository {
    Flux<TimeSlot> findByDeliveryMethod(DeliveryMethod method);
    Mono<TimeSlot> findById(UUID id);
    Mono<TimeSlot> save(TimeSlot timeSlot);
}
