package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.DocumentLetterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentLetter} and its DTO {@link DocumentLetterDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { ContactMapper.class, ContactAddressMapper.class, ContactPersonMapper.class, LayoutMapper.class, SignatureMapper.class }
)
public interface DocumentLetterMapper extends EntityMapper<DocumentLetterDTO, DocumentLetter> {
    @Mapping(target = "contact", source = "contact", qualifiedByName = "id")
    @Mapping(target = "contactAddress", source = "contactAddress", qualifiedByName = "id")
    @Mapping(target = "contactPerson", source = "contactPerson", qualifiedByName = "id")
    @Mapping(target = "contactPrePageAddress", source = "contactPrePageAddress", qualifiedByName = "id")
    @Mapping(target = "layout", source = "layout", qualifiedByName = "id")
    @Mapping(target = "layout", source = "layout", qualifiedByName = "id")
    DocumentLetterDTO toDto(DocumentLetter s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentLetterDTO toDtoId(DocumentLetter documentLetter);
}
