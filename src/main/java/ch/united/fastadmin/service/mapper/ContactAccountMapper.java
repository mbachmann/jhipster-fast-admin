package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactAccount} and its DTO {@link ContactAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContactMapper.class })
public interface ContactAccountMapper extends EntityMapper<ContactAccountDTO, ContactAccount> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    ContactAccountDTO toDto(ContactAccount s);
}
