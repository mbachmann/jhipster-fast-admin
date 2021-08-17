package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.PermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Permission} and its DTO {@link PermissionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {}
