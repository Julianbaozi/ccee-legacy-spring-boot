package br.org.energia.legacy.boot.starter.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder.Builder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;

import br.org.energia.legacy.boot.starter.CCEELegacyProperties;

@Configuration
@AutoConfigureBefore(JpaBaseConfiguration.class)
public class CCEEPersistenceAutoConfiguration {

	private static final Logger LOG = LoggerFactory.getLogger(CCEEPersistenceAutoConfiguration.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private CCEELegacyProperties properties;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setProps(CCEELegacyProperties props) {
		this.properties = props;
	}

	@Autowired
	public void configure(ConfigurableApplicationContext applicationContext, EntityManagerFactoryBuilder builder,
			DataSource datasource) throws BeansException {

		LOG.debug("CCEELegacy: Persistence confs =" + properties.getPersistences().size());

		for (CCEEPersistenceProperties persistence : properties.getPersistences()) {
			LocalContainerEntityManagerFactoryBean bean = createEMF(builder, datasource,
					persistence);
			bean.afterPropertiesSet();
			applicationContext.getBeanFactory().registerSingleton(persistence.getUnitName(), bean);
			LOG.debug("CCEELegacy: PU Created =" + persistence.getUnitName());
		}
	}

	@ConditionalOnProperty(prefix = "spring.jta", name = "enabled", havingValue = "true", matchIfMissing = true)
	@Bean
	public SpringJtaPlatformAdapter adapter(JtaTransactionManager jta) {
		SpringJtaPlatformAdapter adapter = new SpringJtaPlatformAdapter();
		adapter.setJtaTransactionManager(jta);
		return adapter;
	}

	private LocalContainerEntityManagerFactoryBean createEMF(EntityManagerFactoryBuilder builder
															, DataSource dataSource
															, CCEEPersistenceProperties persistenceProperties) {

		Map<String, String> props = new HashMap<>();
		
		Builder emfBuilder = builder.dataSource(dataSource)
				.properties(props)
				.packages(persistenceProperties.getEntityPackage())
				.persistenceUnit(persistenceProperties.getUnitName());

		addVigenciaProperties(persistenceProperties, props);

		addJpqlTranslatorProperties(persistenceProperties, props);

		addDDLProperties(persistenceProperties, props);

		addJtaProperties(props, emfBuilder);

		return emfBuilder.build();
	}

	private void addJtaProperties(Map<String, String> props, Builder emfBuilder) {
		Boolean jtaEnabled = applicationContext.getEnvironment().getProperty("spring.jta.enabled"
																			, Boolean.class
																			, Boolean.TRUE);

		if (jtaEnabled) {
			props.put("hibernate.transaction.jta.platform", "br.org.energia.legacy.boot.starter.persistence.SpringJtaPlatformAdapter");
			emfBuilder.jta(true);
		}
	}

	private void addDDLProperties(CCEEPersistenceProperties persistenceProperties, Map<String, String> props) {
		if (persistenceProperties.isEnabledDDLActions()) {
			props.put("hibernate.hbm2ddl.auto", persistenceProperties.getDdlAction());
		}
	}

	private void addJpqlTranslatorProperties(CCEEPersistenceProperties persistenceProperties,
			Map<String, String> props) {
		if (persistenceProperties.isEnabledJpqlTranslator()) {
			props.put("hibernate.ejb.interceptor", "br.org.energia.hibernate.query.HibernateQueryInterceptor");
		}
	}

	private void addVigenciaProperties(CCEEPersistenceProperties persistenceProperties, Map<String, String> props) {
		if (persistenceProperties.isEnabledVigenciaInterceptor()) {
			props.put("hibernate.ejb.event.create",
					"br.org.energia.commons.csi.vigenciamento.hibernate.HibernateEventInterceptor");
			props.put("hibernate.ejb.event.merge",
					"br.org.energia.commons.csi.vigenciamento.hibernate.HibernateEventInterceptor");
		}
	}

}
