package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.Permission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Permission} entity.
 */
public interface PermissionSearchRepository extends ElasticsearchRepository<Permission, Long> {}
