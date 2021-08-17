package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ExchangeRateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExchangeRate} and its DTO {@link ExchangeRateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExchangeRateMapper extends EntityMapper<ExchangeRateDTO, ExchangeRate> {}
