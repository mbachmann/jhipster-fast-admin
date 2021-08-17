package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ValueAddedTaxDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ValueAddedTax} and its DTO {@link ValueAddedTaxDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ValueAddedTaxMapper extends EntityMapper<ValueAddedTaxDTO, ValueAddedTax> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ValueAddedTaxDTO toDtoId(ValueAddedTax valueAddedTax);
}
