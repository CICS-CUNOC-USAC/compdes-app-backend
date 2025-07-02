package com.compdes.reservations.mappers;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 *
 *
 * @author Yennifer de Leon
 * @version 1.0
 * @since 2025-07-01
 */
@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class ReservationMapper {

}
