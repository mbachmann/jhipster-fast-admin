package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.InvoiceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Invoice} and its DTO {@link InvoiceDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ContactMapper.class,
        ContactAddressMapper.class,
        ContactPersonMapper.class,
        LayoutMapper.class,
        SignatureMapper.class,
        BankAccountMapper.class,
        IsrMapper.class,
    }
)
public interface InvoiceMapper extends EntityMapper<InvoiceDTO, Invoice> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    @Mapping(target = "contactAddress", source = "contactAddress", qualifiedByName = "id")
    @Mapping(target = "contactPerson", source = "contactPerson", qualifiedByName = "id")
    @Mapping(target = "contactPrePageAddress", source = "contactPrePageAddress", qualifiedByName = "id")
    @Mapping(target = "layout", source = "layout", qualifiedByName = "id")
    @Mapping(target = "signature", source = "signature", qualifiedByName = "id")
    @Mapping(target = "bankAccount", source = "bankAccount", qualifiedByName = "id")
    @Mapping(target = "isr", source = "isr", qualifiedByName = "id")
    InvoiceDTO toDto(Invoice s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InvoiceDTO toDtoId(Invoice invoice);
}
