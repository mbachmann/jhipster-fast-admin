package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContactAddressMapper.class })
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "mainAddress", source = "mainAddress", qualifiedByName = "id")
    ContactDTO toDto(Contact s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactDTO toDtoId(Contact contact);
}
