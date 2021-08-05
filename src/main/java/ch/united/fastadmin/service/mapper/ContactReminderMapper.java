package ch.united.fastadmin.service.mapper;

import ch.united.fastadmin.domain.*;
import ch.united.fastadmin.service.dto.ContactReminderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactReminder} and its DTO {@link ContactReminderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactReminderMapper extends EntityMapper<ContactReminderDTO, ContactReminder> {}
