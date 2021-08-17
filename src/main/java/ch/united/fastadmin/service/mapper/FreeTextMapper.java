package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.FreeTextDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FreeText} and its DTO {@link FreeTextDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FreeTextMapper extends EntityMapper<FreeTextDTO, FreeText> {}
