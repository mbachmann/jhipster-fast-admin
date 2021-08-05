package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.Role;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Role} entity.
 */
public interface RoleSearchRepository extends ElasticsearchRepository<Role, Long> {}
