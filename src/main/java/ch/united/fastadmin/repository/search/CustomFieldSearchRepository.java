package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.CustomField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CustomField} entity.
 */
public interface CustomFieldSearchRepository extends ElasticsearchRepository<CustomField, Long> {}
