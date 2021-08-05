package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.ContactGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ContactGroup} entity.
 */
public interface ContactGroupSearchRepository extends ElasticsearchRepository<ContactGroup, Long> {}
