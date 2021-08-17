package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.BankAccountDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankAccount} and its DTO {@link BankAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankAccountMapper extends EntityMapper<BankAccountDTO, BankAccount> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankAccountDTO toDtoId(BankAccount bankAccount);
}
