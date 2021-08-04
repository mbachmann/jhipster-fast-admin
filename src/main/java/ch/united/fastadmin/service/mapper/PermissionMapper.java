package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.PermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoleMapper.class, ContactMapper.class, ContactAddressMapper.class })
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
    @Mapping(target = "role", source = "role", qualifiedByName = "id")
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    @Mapping(target = "contactAddress", source = "contactAddress", qualifiedByName = "id")
    PermissionDTO toDto(Permission s);
}
