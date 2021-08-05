package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.ContactPerson;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ContactPerson} entity.
 */
public interface ContactPersonSearchRepository extends ElasticsearchRepository<ContactPerson, Long> {}
