package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.OfferDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Offer} and its DTO {@link OfferDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ContactMapper.class, ContactAddressMapper.class, ContactPersonMapper.class, LayoutMapper.class, SignatureMapper.class }
)
public interface OfferMapper extends EntityMapper<OfferDTO, Offer> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    @Mapping(target = "contactAddress", source = "contactAddress", qualifiedByName = "id")
    @Mapping(target = "contactPerson", source = "contactPerson", qualifiedByName = "id")
    @Mapping(target = "contactPrePageAddress", source = "contactPrePageAddress", qualifiedByName = "id")
    @Mapping(target = "layout", source = "layout", qualifiedByName = "id")
    @Mapping(target = "layout", source = "layout", qualifiedByName = "id")
    OfferDTO toDto(Offer s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OfferDTO toDtoId(Offer offer);
}
