package com.hexagonale.carrefour.delivery.delivery_service.application.service;

import com.hexagonale.carrefour.delivery.delivery_service.domain.exception.ResourceNotFoundException;
import com.hexagonale.carrefour.delivery.delivery_service.domain.model.DeliveryMethod;
import com.hexagonale.carrefour.delivery.delivery_service.domain.model.TimeSlot;
import com.hexagonale.carrefour.delivery.delivery_service.domain.port.TimeSlotRepository;
import com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.dto.TimeSlotDTO;
import com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.mapper.TimeSlotMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class DeliveryServiceImplTest {

    private TimeSlotRepository repository;
    private TimeSlotMapper mapper;
    private DeliveryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(TimeSlotRepository.class);
        mapper = mock(TimeSlotMapper.class);
        service = new DeliveryServiceImpl(repository, mapper);
    }

    @Test
    void testGetAvailableMethods() {
        StepVerifier.create(service.getAvailableMethods())
                .expectNext("DRIVE", "DELIVERY", "DELIVERY_TODAY", "DELIVERY_ASAP")
                .verifyComplete();
    }

    @Test
    void testGetAvailableTimeSlots_validMethod() {
        TimeSlot slot = new TimeSlot(UUID.randomUUID(), DeliveryMethod.DRIVE, LocalDate.now(), LocalTime.NOON, LocalTime.MAX, false);
        TimeSlotDTO dto = mock(TimeSlotDTO.class);

        when(repository.findByDeliveryMethod(DeliveryMethod.DRIVE)).thenReturn(Flux.just(slot));
        when(mapper.toDTO(slot)).thenReturn(dto);

        StepVerifier.create(service.getAvailableTimeSlots(DeliveryMethod.DRIVE))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void testGetAvailableTimeSlots_notFound() {
        when(repository.findByDeliveryMethod(DeliveryMethod.DRIVE)).thenReturn(Flux.empty());

        StepVerifier.create(service.getAvailableTimeSlots(DeliveryMethod.DRIVE))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void testReserveTimeSlot_success() {
        UUID id = UUID.randomUUID();
        TimeSlot slot = new TimeSlot(id, DeliveryMethod.DRIVE, LocalDate.now(), LocalTime.NOON, LocalTime.MAX, false);
        TimeSlot reserved = new TimeSlot(id, DeliveryMethod.DRIVE, LocalDate.now(), LocalTime.NOON, LocalTime.MAX, true);
        TimeSlotDTO dto = mock(TimeSlotDTO.class);

        when(repository.findById(id)).thenReturn(Mono.just(slot));
        when(repository.save(any(TimeSlot.class))).thenReturn(Mono.just(reserved));
        when(mapper.toDTO(reserved)).thenReturn(dto);

        StepVerifier.create(service.reserveTimeSlot(id))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void testReserveTimeSlot_notFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(service.reserveTimeSlot(id))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void testReserveTimeSlot_alreadyReserved() {
        UUID id = UUID.randomUUID();
        TimeSlot slot = new TimeSlot(id, DeliveryMethod.DRIVE, LocalDate.now(), LocalTime.NOON, LocalTime.MAX, true);
        when(repository.findById(id)).thenReturn(Mono.just(slot));

        StepVerifier.create(service.reserveTimeSlot(id))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
