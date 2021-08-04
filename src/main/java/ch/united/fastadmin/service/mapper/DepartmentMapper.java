package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.DepartmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoId(Department department);
}
