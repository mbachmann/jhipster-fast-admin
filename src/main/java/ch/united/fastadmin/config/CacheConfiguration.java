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
            createCache(cm, ch.united.fastadmin.domain.Owner.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Contact.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Contact.class.getName() + ".customFields");
            createCache(cm, ch.united.fastadmin.domain.Contact.class.getName() + ".relations");
            createCache(cm, ch.united.fastadmin.domain.Contact.class.getName() + ".groups");
            createCache(cm, ch.united.fastadmin.domain.ContactRelation.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ContactRelation.class.getName() + ".contacts");
            createCache(cm, ch.united.fastadmin.domain.ContactAddress.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ContactGroup.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ContactGroup.class.getName() + ".contacts");
            createCache(cm, ch.united.fastadmin.domain.ContactPerson.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ContactPerson.class.getName() + ".customFields");
            createCache(cm, ch.united.fastadmin.domain.ContactAccount.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ContactReminder.class.getName());
            createCache(cm, ch.united.fastadmin.domain.BankAccount.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ExchangeRate.class.getName());
            createCache(cm, ch.united.fastadmin.domain.CatalogCategory.class.getName());
            createCache(cm, ch.united.fastadmin.domain.CatalogUnit.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ValueAddedTax.class.getName());
            createCache(cm, ch.united.fastadmin.domain.CatalogProduct.class.getName());
            createCache(cm, ch.united.fastadmin.domain.CatalogProduct.class.getName() + ".customFields");
            createCache(cm, ch.united.fastadmin.domain.CatalogService.class.getName());
            createCache(cm, ch.united.fastadmin.domain.CatalogService.class.getName() + ".customFields");
            createCache(cm, ch.united.fastadmin.domain.DocumentLetter.class.getName());
            createCache(cm, ch.united.fastadmin.domain.DocumentLetter.class.getName() + ".customFields");
            createCache(cm, ch.united.fastadmin.domain.DocumentLetter.class.getName() + ".freeTexts");
            createCache(cm, ch.united.fastadmin.domain.DeliveryNote.class.getName());
            createCache(cm, ch.united.fastadmin.domain.DeliveryNote.class.getName() + ".customFields");
            createCache(cm, ch.united.fastadmin.domain.DeliveryNote.class.getName() + ".freeTexts");
            createCache(cm, ch.united.fastadmin.domain.DeliveryNote.class.getName() + ".texts");
            createCache(cm, ch.united.fastadmin.domain.DeliveryNote.class.getName() + ".positions");
            createCache(cm, ch.united.fastadmin.domain.Invoice.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Invoice.class.getName() + ".freeTexts");
            createCache(cm, ch.united.fastadmin.domain.Invoice.class.getName() + ".texts");
            createCache(cm, ch.united.fastadmin.domain.Invoice.class.getName() + ".positions");
            createCache(cm, ch.united.fastadmin.domain.Offer.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Offer.class.getName() + ".freeTexts");
            createCache(cm, ch.united.fastadmin.domain.Offer.class.getName() + ".texts");
            createCache(cm, ch.united.fastadmin.domain.Offer.class.getName() + ".positions");
            createCache(cm, ch.united.fastadmin.domain.OrderConfirmation.class.getName());
            createCache(cm, ch.united.fastadmin.domain.OrderConfirmation.class.getName() + ".freeTexts");
            createCache(cm, ch.united.fastadmin.domain.OrderConfirmation.class.getName() + ".texts");
            createCache(cm, ch.united.fastadmin.domain.OrderConfirmation.class.getName() + ".positions");
            createCache(cm, ch.united.fastadmin.domain.DocumentFreeText.class.getName());
            createCache(cm, ch.united.fastadmin.domain.FreeText.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Signature.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Layout.class.getName());
            createCache(cm, ch.united.fastadmin.domain.DocumentPosition.class.getName());
            createCache(cm, ch.united.fastadmin.domain.DescriptiveDocumentText.class.getName());
            createCache(cm, ch.united.fastadmin.domain.DocumentText.class.getName());
            createCache(cm, ch.united.fastadmin.domain.DocumentInvoiceWorkflow.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Isr.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ReportingActivity.class.getName());
            createCache(cm, ch.united.fastadmin.domain.CostUnit.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Effort.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Project.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Project.class.getName() + ".customFields");
            createCache(cm, ch.united.fastadmin.domain.WorkingHour.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ApplicationRole.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ApplicationUser.class.getName());
            createCache(cm, ch.united.fastadmin.domain.CustomFieldValue.class.getName());
            createCache(cm, ch.united.fastadmin.domain.CustomField.class.getName());
            createCache(cm, ch.united.fastadmin.domain.ResourcePermission.class.getName());
            createCache(cm, ch.united.fastadmin.domain.Permission.class.getName());
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
