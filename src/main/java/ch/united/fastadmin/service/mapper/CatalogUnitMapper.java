package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CatalogUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogUnit} and its DTO {@link CatalogUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatalogUnitMapper extends EntityMapper<CatalogUnitDTO, CatalogUnit> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CatalogUnitDTO toDtoId(CatalogUnit catalogUnit);
}
