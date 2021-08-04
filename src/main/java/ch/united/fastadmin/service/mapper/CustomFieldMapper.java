package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CustomFieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomField} and its DTO {@link CustomFieldDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContactMapper.class })
public interface CustomFieldMapper extends EntityMapper<CustomFieldDTO, CustomField> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    CustomFieldDTO toDto(CustomField s);
}
