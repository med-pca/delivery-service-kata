package com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.mapper;



import com.hexagonale.carrefour.delivery.delivery_service.domain.model.TimeSlot;
import com.hexagonale.carrefour.delivery.delivery_service.infrastructure.web.dto.TimeSlotDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper {

    TimeSlotMapper INSTANCE = Mappers.getMapper(TimeSlotMapper.class);

    TimeSlotDTO toDTO(TimeSlot timeSlot);
}
