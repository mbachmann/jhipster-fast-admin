package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactRelationDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactRelation} and its DTO {@link ContactRelationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactRelationMapper extends EntityMapper<ContactRelationDTO, ContactRelation> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ContactRelationDTO> toDtoIdSet(Set<ContactRelation> contactRelation);
}
