package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactGroupDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactGroup} and its DTO {@link ContactGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactGroupMapper extends EntityMapper<ContactGroupDTO, ContactGroup> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ContactGroupDTO> toDtoIdSet(Set<ContactGroup> contactGroup);
}
