package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.ContactAddress;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ContactAddress} entity.
 */
public interface ContactAddressSearchRepository extends ElasticsearchRepository<ContactAddress, Long> {}
