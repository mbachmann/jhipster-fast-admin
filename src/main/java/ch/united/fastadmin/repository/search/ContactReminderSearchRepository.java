package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.ContactReminder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ContactReminder} entity.
 */
public interface ContactReminderSearchRepository extends ElasticsearchRepository<ContactReminder, Long> {}
