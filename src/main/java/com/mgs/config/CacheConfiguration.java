package com.mgs.config;

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

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
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
            createCache(cm, com.mgs.domain.SystemUser.class.getName());
            createCache(cm, com.mgs.domain.Plan.class.getName());
            createCache(cm, com.mgs.domain.Tenant.class.getName());
            createCache(cm, com.mgs.domain.TenantSubscription.class.getName());
            createCache(cm, com.mgs.domain.TenantEncryptionKey.class.getName());
            createCache(cm, com.mgs.domain.Country.class.getName());
            createCache(cm, com.mgs.domain.State.class.getName());
            createCache(cm, com.mgs.domain.City.class.getName());
            createCache(cm, com.mgs.domain.Address.class.getName());
            createCache(cm, com.mgs.domain.TenantProfile.class.getName());
            createCache(cm, com.mgs.domain.TenantBranding.class.getName());
            createCache(cm, com.mgs.domain.User.class.getName());
            createCache(cm, com.mgs.domain.Role.class.getName());
            createCache(cm, com.mgs.domain.UserRole.class.getName());
            createCache(cm, com.mgs.domain.PermissionModule.class.getName());
            createCache(cm, com.mgs.domain.RolePermission.class.getName());
            createCache(cm, com.mgs.domain.UserHierarchy.class.getName());
            createCache(cm, com.mgs.domain.Department.class.getName());
            createCache(cm, com.mgs.domain.UserDepartment.class.getName());
            createCache(cm, com.mgs.domain.Customer.class.getName());
            createCache(cm, com.mgs.domain.Contact.class.getName());
            createCache(cm, com.mgs.domain.LeadSource.class.getName());
            createCache(cm, com.mgs.domain.Pipeline.class.getName());
            createCache(cm, com.mgs.domain.SubPipeline.class.getName());
            createCache(cm, com.mgs.domain.Product.class.getName());
            createCache(cm, com.mgs.domain.Lead.class.getName());
            createCache(cm, com.mgs.domain.Deal.class.getName());
            createCache(cm, com.mgs.domain.Quotation.class.getName());
            createCache(cm, com.mgs.domain.QuotationItem.class.getName());
            createCache(cm, com.mgs.domain.ComplaintCategory.class.getName());
            createCache(cm, com.mgs.domain.Complaint.class.getName());
            createCache(cm, com.mgs.domain.Ticket.class.getName());
            createCache(cm, com.mgs.domain.TaskType.class.getName());
            createCache(cm, com.mgs.domain.Task.class.getName());
            createCache(cm, com.mgs.domain.TaskComment.class.getName());
            createCache(cm, com.mgs.domain.Event.class.getName());
            createCache(cm, com.mgs.domain.EventTaskAssignment.class.getName());
            createCache(cm, com.mgs.domain.Notification.class.getName());
            createCache(cm, com.mgs.domain.EventNotification.class.getName());
            createCache(cm, com.mgs.domain.ReportTemplate.class.getName());
            createCache(cm, com.mgs.domain.ReportRun.class.getName());
            createCache(cm, com.mgs.domain.AuditLog.class.getName());
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
