package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.DescriptiveDocumentTextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DescriptiveDocumentText} and its DTO {@link DescriptiveDocumentTextDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DeliveryNoteMapper.class, InvoiceMapper.class, OfferMapper.class, OrderConfirmationMapper.class }
)
public interface DescriptiveDocumentTextMapper extends EntityMapper<DescriptiveDocumentTextDTO, DescriptiveDocumentText> {
    @Mapping(target = "deliveryNote", source = "deliveryNote", qualifiedByName = "id")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "id")
    @Mapping(target = "offer", source = "offer", qualifiedByName = "id")
    @Mapping(target = "orderConfirmation", source = "orderConfirmation", qualifiedByName = "id")
    DescriptiveDocumentTextDTO toDto(DescriptiveDocumentText s);
}
