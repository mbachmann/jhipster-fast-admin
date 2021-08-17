package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.VatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Vat} and its DTO {@link VatDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VatMapper extends EntityMapper<VatDTO, Vat> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VatDTO toDtoId(Vat vat);
}
