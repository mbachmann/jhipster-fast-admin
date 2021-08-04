package ch.united.fastadmin.repository.search;

import ch.united.fastadmin.domain.Country;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Country} entity.
 */
public interface CountrySearchRepository extends ElasticsearchRepository<Country, Long> {}
