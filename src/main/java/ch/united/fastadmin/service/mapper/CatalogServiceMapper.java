package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CatalogServiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogService} and its DTO {@link CatalogServiceDTO}.
 */
@Mapper(componentModel = "spring", uses = { CatalogCategoryMapper.class, CatalogUnitMapper.class, VatMapper.class })
public interface CatalogServiceMapper extends EntityMapper<CatalogServiceDTO, CatalogService> {
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "id")
    @Mapping(target = "vat", source = "vat", qualifiedByName = "id")
    CatalogServiceDTO toDto(CatalogService s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CatalogServiceDTO toDtoId(CatalogService catalogService);
}
