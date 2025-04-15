package com.hexagonale.carrefour.delivery.delivery_service.infrastructure.persistence;

import com.hexagonale.carrefour.delivery.delivery_service.domain.model.DeliveryMethod;
import com.hexagonale.carrefour.delivery.delivery_service.domain.model.TimeSlot;
import com.hexagonale.carrefour.delivery.delivery_service.domain.port.TimeSlotRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTimeSlotRepository implements TimeSlotRepository {

    private final Map<UUID, TimeSlot> timeSlotMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        for (DeliveryMethod method : DeliveryMethod.values()) {
            for (int i = 0; i < 5; i++) {
                UUID id = UUID.randomUUID();
                timeSlotMap.put(id, new TimeSlot(
                        id,
                        method,
                        LocalDate.now().plusDays(1),
                        LocalTime.of(9 + i, 0),
                        LocalTime.of(10 + i, 0),
                        false
                ));
            }
        }
    }

    @Override
    public Flux<TimeSlot> findByDeliveryMethod(DeliveryMethod method) {
        return Flux.fromStream(timeSlotMap.values().stream()
                .filter(ts -> ts.getMethod() == method));
    }

    @Override
    public Mono<TimeSlot> findById(UUID id) {
        return Mono.justOrEmpty(timeSlotMap.get(id));
    }

    @Override
    public Mono<TimeSlot> save(TimeSlot timeSlot) {
        timeSlotMap.put(timeSlot.getId(), timeSlot);
        return Mono.just(timeSlot);
    }
}
