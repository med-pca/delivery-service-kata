package com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web;

import com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.dto.TimeSlotDTO;
import com.hexagonale.carrefour.delivery.delivery_service.domain.port.DeliveryService;
import com.hexagonale.carrefour.delivery.delivery_service.domain.model.DeliveryMethod;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/methods")
    public Mono<List<String>> getDeliveryMethods() {
        return deliveryService.getAvailableMethods().collectList();
    }

    @GetMapping("/slots/{code}")
    public Flux<TimeSlotDTO> getSlots(@PathVariable String code) {
        DeliveryMethod method = DeliveryMethod.valueOf(code.toUpperCase());
        return deliveryService.getAvailableTimeSlots(method);
    }

    @PostMapping("/slots/{id}/reserve")
    public Mono<TimeSlotDTO> reserveSlot(@PathVariable UUID id) {
        return deliveryService.reserveTimeSlot(id);
    }
}
