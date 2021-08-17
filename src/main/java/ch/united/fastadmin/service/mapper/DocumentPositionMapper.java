package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.DocumentPositionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentPosition} and its DTO {@link DocumentPositionDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { CatalogUnitMapper.class, DeliveryNoteMapper.class, InvoiceMapper.class, OfferMapper.class, OrderConfirmationMapper.class }
)
public interface DocumentPositionMapper extends EntityMapper<DocumentPositionDTO, DocumentPosition> {
    @Mapping(target = "unit", source = "unit", qualifiedByName = "id")
    @Mapping(target = "deliveryNote", source = "deliveryNote", qualifiedByName = "id")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "id")
    @Mapping(target = "offer", source = "offer", qualifiedByName = "id")
    @Mapping(target = "orderConfirmation", source = "orderConfirmation", qualifiedByName = "id")
    DocumentPositionDTO toDto(DocumentPosition s);
}
