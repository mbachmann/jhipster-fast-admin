package ch.united.fastadmin.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CustomFieldSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CustomFieldSearchRepositoryMockConfiguration {

    @MockBean
    private CustomFieldSearchRepository mockCustomFieldSearchRepository;
}