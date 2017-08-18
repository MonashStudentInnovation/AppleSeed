package com.threewks.springboot.gaetools.configuration;

import com.googlecode.objectify.ObjectifyFilter;
import com.threewks.gaetools.objectify.ObjectifyModule;
import com.threewks.gaetools.search.SearchModule;
import com.threewks.gaetools.search.gae.SearchConfig;
import com.threewks.gaetools.search.gae.meta.IndexTypeLookup;
import com.threewks.gaetools.transformer.TransformerManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;

@Configuration
public class GaeToolsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ObjectifyFilter objectifyFilter() {
        return new ObjectifyFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public TransformerManager defaultTransformerManager() {
        return ObjectifyModule.defaultTransformerManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public SearchConfig searchConfig(TransformerManager transformerManager, IndexTypeLookup indexTypeLookup) {
        return new SearchConfig(transformerManager, SearchModule.defaultFieldMediatorSet(), indexTypeLookup);
    }

    @Bean
    @ConditionalOnMissingBean
    public IndexTypeLookup defaultIndexTypeLookup() {
        return SearchModule.defaultIndexTypeLookup();
    }


    @PostConstruct
    public void init() {
        ObjectifyModule.addDefaultTranslators();
    }

}
