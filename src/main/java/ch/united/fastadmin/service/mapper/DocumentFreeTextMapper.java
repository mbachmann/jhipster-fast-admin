package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.DocumentFreeTextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentFreeText} and its DTO {@link DocumentFreeTextDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { DocumentLetterMapper.class, DeliveryNoteMapper.class, InvoiceMapper.class, OfferMapper.class, OrderConfirmationMapper.class }
)
public interface DocumentFreeTextMapper extends EntityMapper<DocumentFreeTextDTO, DocumentFreeText> {
    @Mapping(target = "documentLetter", source = "documentLetter", qualifiedByName = "id")
    @Mapping(target = "deliveryNote", source = "deliveryNote", qualifiedByName = "id")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "id")
    @Mapping(target = "offer", source = "offer", qualifiedByName = "id")
    @Mapping(target = "orderConfirmation", source = "orderConfirmation", qualifiedByName = "id")
    DocumentFreeTextDTO toDto(DocumentFreeText s);
}
