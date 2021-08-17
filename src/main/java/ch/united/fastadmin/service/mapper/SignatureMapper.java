package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.SignatureDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Signature} and its DTO {@link SignatureDTO}.
 */
@Mapper(componentModel = "spring", uses = { ApplicationUserMapper.class })
public interface SignatureMapper extends EntityMapper<SignatureDTO, Signature> {
    @Mapping(target = "applicationUser", source = "applicationUser", qualifiedByName = "id")
    SignatureDTO toDto(Signature s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SignatureDTO toDtoId(Signature signature);
}
