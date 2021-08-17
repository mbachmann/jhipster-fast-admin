package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ApplicationUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ApplicationUserDTO toDtoId(ApplicationUser applicationUser);
}
