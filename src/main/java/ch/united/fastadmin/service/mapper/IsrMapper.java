package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.IsrDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Isr} and its DTO {@link IsrDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IsrMapper extends EntityMapper<IsrDTO, Isr> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    IsrDTO toDtoId(Isr isr);
}
