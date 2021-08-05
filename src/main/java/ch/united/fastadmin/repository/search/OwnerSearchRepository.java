package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.Owner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Owner} entity.
 */
public interface OwnerSearchRepository extends ElasticsearchRepository<Owner, Long> {}
