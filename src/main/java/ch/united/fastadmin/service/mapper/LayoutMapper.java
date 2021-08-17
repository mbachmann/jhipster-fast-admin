package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.LayoutDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Layout} and its DTO {@link LayoutDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LayoutMapper extends EntityMapper<LayoutDTO, Layout> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LayoutDTO toDtoId(Layout layout);
}
