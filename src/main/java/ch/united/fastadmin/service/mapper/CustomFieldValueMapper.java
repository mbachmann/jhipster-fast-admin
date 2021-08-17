package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CustomFieldValueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomFieldValue} and its DTO {@link CustomFieldValueDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        CustomFieldMapper.class,
        ContactMapper.class,
        ContactPersonMapper.class,
        ProjectMapper.class,
        CatalogProductMapper.class,
        CatalogServiceMapper.class,
        DocumentLetterMapper.class,
        DeliveryNoteMapper.class,
    }
)
public interface CustomFieldValueMapper extends EntityMapper<CustomFieldValueDTO, CustomFieldValue> {
    @Mapping(target = "customField", source = "customField", qualifiedByName = "id")
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    @Mapping(target = "contactPerson", source = "contactPerson", qualifiedByName = "id")
    @Mapping(target = "project", source = "project", qualifiedByName = "id")
    @Mapping(target = "catalogProduct", source = "catalogProduct", qualifiedByName = "id")
    @Mapping(target = "catalogService", source = "catalogService", qualifiedByName = "id")
    @Mapping(target = "documentLetter", source = "documentLetter", qualifiedByName = "id")
    @Mapping(target = "deliveryNote", source = "deliveryNote", qualifiedByName = "id")
    CustomFieldValueDTO toDto(CustomFieldValue s);
}
