package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.CostUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CostUnit} and its DTO {@link CostUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CostUnitMapper extends EntityMapper<CostUnitDTO, CostUnit> {}
