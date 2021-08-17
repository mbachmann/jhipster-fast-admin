package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ResourcePermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourcePermission} and its DTO {@link ResourcePermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicationRoleMapper.class, ApplicationUserMapper.class })
public interface ResourcePermissionMapper extends EntityMapper<ResourcePermissionDTO, ResourcePermission> {
    @Mapping(target = "role", source = "role", qualifiedByName = "id")
    @Mapping(target = "applicationUser", source = "applicationUser", qualifiedByName = "id")
    ResourcePermissionDTO toDto(ResourcePermission s);
}
