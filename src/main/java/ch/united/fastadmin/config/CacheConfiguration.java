package ch.united.fastadmin.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, ch.united.fastadmin.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, ch.united.fastadmin.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, ch.united.fastadmin.domain.User.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Authority.class.getName());
            createCache(cm, ch.united.fastadmin.domain.User.class.getName() + ".authorities");
            createCache(cm, ch.united.fastadmin.domain.Region.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Country.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Country.class.getName() + ".regions");
            createCache(cm, ch.united.fastadmin.domain.Location.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Location.class.getName() + ".countries");
            createCache(cm, ch.united.fastadmin.domain.Department.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Department.class.getName() + ".locations");
            createCache(cm, ch.united.fastadmin.domain.Department.class.getName() + ".employees");
            createCache(cm, ch.united.fastadmin.domain.Task.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Task.class.getName() + ".jobs");
            createCache(cm, ch.united.fastadmin.domain.Employee.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Employee.class.getName() + ".jobs");
            createCache(cm, ch.united.fastadmin.domain.Job.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Job.class.getName() + ".tasks");
            createCache(cm, ch.united.fastadmin.domain.JobHistory.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Contact.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Contact.class.getName() + ".permissions");
            createCache(cm, ch.united.fastadmin.domain.Contact.class.getName() + ".groups");
            createCache(cm, ch.united.fastadmin.domain.Contact.class.getName() + ".customFields");
            createCache(cm, ch.united.fastadmin.domain.CustomField.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ContactAddress.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ContactAddress.class.getName() + ".permissions");
            createCache(cm, ch.united.fastadmin.domain.ContactGroup.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Permission.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Role.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
