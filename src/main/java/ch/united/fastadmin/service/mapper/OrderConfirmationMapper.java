package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.OrderConfirmationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderConfirmation} and its DTO {@link OrderConfirmationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ContactMapper.class, ContactAddressMapper.class, ContactPersonMapper.class, LayoutMapper.class, SignatureMapper.class }
)
public interface OrderConfirmationMapper extends EntityMapper<OrderConfirmationDTO, OrderConfirmation> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    @Mapping(target = "contactAddress", source = "contactAddress", qualifiedByName = "id")
    @Mapping(target = "contactPerson", source = "contactPerson", qualifiedByName = "id")
    @Mapping(target = "contactPrePageAddress", source = "contactPrePageAddress", qualifiedByName = "id")
    @Mapping(target = "layout", source = "layout", qualifiedByName = "id")
    @Mapping(target = "signature", source = "signature", qualifiedByName = "id")
    OrderConfirmationDTO toDto(OrderConfirmation s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrderConfirmationDTO toDtoId(OrderConfirmation orderConfirmation);
}
