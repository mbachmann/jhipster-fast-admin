package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactGroup} and its DTO {@link ContactGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContactMapper.class })
public interface ContactGroupMapper extends EntityMapper<ContactGroupDTO, ContactGroup> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    ContactGroupDTO toDto(ContactGroup s);
}
