package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.DocumentTextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentText} and its DTO {@link DocumentTextDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocumentTextMapper extends EntityMapper<DocumentTextDTO, DocumentText> {}
