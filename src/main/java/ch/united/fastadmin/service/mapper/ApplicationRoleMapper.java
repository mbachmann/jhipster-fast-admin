package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ApplicationRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationRole} and its DTO {@link ApplicationRoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationRoleMapper extends EntityMapper<ApplicationRoleDTO, ApplicationRole> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationRoleDTO toDtoId(ApplicationRole applicationRole);
}
