package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CatalogProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogProduct} and its DTO {@link CatalogProductDTO}.
 */
@Mapper(componentModel = "spring", uses = { CatalogCategoryMapper.class, CatalogUnitMapper.class, VatMapper.class })
public interface CatalogProductMapper extends EntityMapper<CatalogProductDTO, CatalogProduct> {
    @Mapping(target = "category", source = "category", qualifiedByName = "id")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "id")
    @Mapping(target = "vat", source = "vat", qualifiedByName = "id")
    CatalogProductDTO toDto(CatalogProduct s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CatalogProductDTO toDtoId(CatalogProduct catalogProduct);
}
