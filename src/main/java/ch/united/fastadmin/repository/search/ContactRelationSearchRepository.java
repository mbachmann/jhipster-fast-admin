package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.ContactRelation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ContactRelation} entity.
 */
public interface ContactRelationSearchRepository extends ElasticsearchRepository<ContactRelation, Long> {}
