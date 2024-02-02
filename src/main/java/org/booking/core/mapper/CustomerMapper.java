package org.booking.core.mapper;

import org.booking.core.domain.dto.CustomerDto;
import org.booking.core.domain.entity.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDto toDto(Customer obj);

    Customer dtoTo(CustomerDto businessDto);

}