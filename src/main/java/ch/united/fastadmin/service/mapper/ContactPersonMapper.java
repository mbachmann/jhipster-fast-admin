package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactPersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactPerson} and its DTO {@link ContactPersonDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContactMapper.class })
public interface ContactPersonMapper extends EntityMapper<ContactPersonDTO, ContactPerson> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    ContactPersonDTO toDto(ContactPerson s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactPersonDTO toDtoId(ContactPerson contactPerson);
}
