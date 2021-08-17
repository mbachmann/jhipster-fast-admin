package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ActivityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activity} and its DTO {@link ActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = { CatalogServiceMapper.class })
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {
    @Mapping(target = "activity", source = "activity", qualifiedByName = "id")
    ActivityDTO toDto(Activity s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActivityDTO toDtoId(Activity activity);
}
