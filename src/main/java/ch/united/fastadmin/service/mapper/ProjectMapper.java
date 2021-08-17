package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ProjectDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring", uses = { ContactMapper.class })
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    ProjectDTO toDto(Project s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectDTO toDtoId(Project project);
}
