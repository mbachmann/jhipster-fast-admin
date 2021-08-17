package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactAddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactAddress} and its DTO {@link ContactAddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContactMapper.class })
public interface ContactAddressMapper extends EntityMapper<ContactAddressDTO, ContactAddress> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    ContactAddressDTO toDto(ContactAddress s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactAddressDTO toDtoId(ContactAddress contactAddress);
}
