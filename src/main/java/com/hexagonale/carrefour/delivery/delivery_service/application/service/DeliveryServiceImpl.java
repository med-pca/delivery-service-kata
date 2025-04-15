package com.hexagonale.carrefour.delivery.delivery_service.application.service;

import com.hexagonale.carrefour.delivery.delivery_service.domain.exception.ResourceNotFoundException;
import com.hexagonale.carrefour.delivery.delivery_service.domain.model.DeliveryMethod;
import com.hexagonale.carrefour.delivery.delivery_service.domain.port.DeliveryService;
import com.hexagonale.carrefour.delivery.delivery_service.domain.port.TimeSlotRepository;
import com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.dto.TimeSlotDTO;
import com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.mapper.TimeSlotMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotMapper timeSlotMapper;

    public DeliveryServiceImpl(TimeSlotRepository timeSlotRepository, TimeSlotMapper timeSlotMapper) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotMapper = timeSlotMapper;
    }

    @Override
    public Flux<String> getAvailableMethods() {
        return Flux.fromArray(DeliveryMethod.values())
                .map(Enum::name);
    }


    @Override
    public Flux<TimeSlotDTO> getAvailableTimeSlots(DeliveryMethod method) {
        if (method == null) {
            return Flux.error(new IllegalArgumentException("Delivery method must not be null"));
        }

        return timeSlotRepository.findByDeliveryMethod(method)
                .switchIfEmpty(Flux.error(new ResourceNotFoundException("No time slots found for delivery method: " + method.name())))
                .map(timeSlotMapper::toDTO);
    }

    @Override
    public Mono<TimeSlotDTO> reserveTimeSlot(UUID id) {
        return timeSlotRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("TimeSlot not found: " + id)))
                .filter(ts -> !ts.isReserved())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("TimeSlot already reserved")))
                .map(ts -> {
                    ts.reserve();
                    return ts;
                })
                .flatMap(timeSlotRepository::save)
                .map(timeSlotMapper::toDTO);
    }
}
