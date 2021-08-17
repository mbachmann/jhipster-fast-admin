package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CustomFieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomField} and its DTO {@link CustomFieldDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomFieldMapper extends EntityMapper<CustomFieldDTO, CustomField> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomFieldDTO toDtoId(CustomField customField);
}
