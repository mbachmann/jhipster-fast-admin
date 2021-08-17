package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CatalogCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogCategory} and its DTO {@link CatalogCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatalogCategoryMapper extends EntityMapper<CatalogCategoryDTO, CatalogCategory> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CatalogCategoryDTO toDtoId(CatalogCategory catalogCategory);
}
