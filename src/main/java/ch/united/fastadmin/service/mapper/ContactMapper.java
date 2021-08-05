package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contact} and its DTO {@link ContactDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContactAddressMapper.class, ContactRelationMapper.class })
public interface ContactMapper extends EntityMapper<ContactDTO, Contact> {
    @Mapping(target = "contactMainAddress", source = "contactMainAddress", qualifiedByName = "id")
    @Mapping(target = "relations", source = "relations", qualifiedByName = "idSet")
    ContactDTO toDto(Contact s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactDTO toDtoId(Contact contact);

    @Mapping(target = "removeRelations", ignore = true)
    Contact toEntity(ContactDTO contactDTO);
}
