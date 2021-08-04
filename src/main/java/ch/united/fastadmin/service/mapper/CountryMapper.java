package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring", uses = { LocationMapper.class })
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {
    @Mapping(target = "location", source = "location", qualifiedByName = "id")
    CountryDTO toDto(Country s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoId(Country country);
}