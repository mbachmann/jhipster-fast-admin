package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactAddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactAddress} and its DTO {@link ContactAddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactAddressMapper extends EntityMapper<ContactAddressDTO, ContactAddress> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactAddressDTO toDtoId(ContactAddress contactAddress);
}
