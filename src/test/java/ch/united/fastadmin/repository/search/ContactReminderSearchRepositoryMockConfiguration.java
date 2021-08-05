package ch.united.fastadmin.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ContactReminderSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ContactReminderSearchRepositoryMockConfiguration {

    @MockBean
    private ContactReminderSearchRepository mockContactReminderSearchRepository;
}
