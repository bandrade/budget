/**
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dupont.budget.bpm.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.jbpm.kie.services.impl.audit.ServicesAwareAuditEventBuilder;
import org.jbpm.process.audit.AbstractAuditLogger;
import org.jbpm.process.audit.AuditLoggerFactory;
import org.jbpm.runtime.manager.impl.cdi.InjectableRegisterableItemsFactory;
import org.jbpm.services.task.identity.DefaultUserInfo;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.internal.runtime.manager.cdi.qualifier.PerProcessInstance;
import org.kie.internal.runtime.manager.cdi.qualifier.PerRequest;
import org.kie.internal.runtime.manager.cdi.qualifier.Singleton;
import org.kie.internal.task.api.UserInfo;

@ApplicationScoped
public class DupontApplicationScopedProducer {

    @PersistenceUnit(unitName = "org.jbpm.domain")
    private EntityManagerFactory emf;


    @Inject
    private BeanManager beanManager;


    @Produces
    public EntityManagerFactory produceEntityManagerFactory() {
        if (this.emf == null) {
            this.emf = Persistence
                    .createEntityManagerFactory("org.jbpm.domain");
        }
        return this.emf;
    }

    @Produces
    @Singleton
    @PerProcessInstance
    @PerRequest
    public RuntimeEnvironment produceEnvironment(EntityManagerFactory emf) {
    	KieServices kieServices = KieServices.Factory.get();
    	ReleaseId releaseId = kieServices.newReleaseId( "com.dupont.budget", "budget-bpm-repo", "1.0.13" );
    	String deploymentId = releaseId.toString();
    	AbstractAuditLogger auditLogger =AuditLoggerFactory.newJPAInstance();
    	ServicesAwareAuditEventBuilder auditEventBuilder = new ServicesAwareAuditEventBuilder();
    	auditEventBuilder.setDeploymentUnitId(deploymentId);
    	auditEventBuilder.setIdentityProvider(new CustomIdentityProvider());
    	auditLogger.setBuilder(auditEventBuilder);
        RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get()
                .newDefaultBuilder(releaseId)
                .entityManagerFactory(emf).
                registerableItemsFactory(InjectableRegisterableItemsFactory.getFactory(beanManager, auditLogger)).get();

        return environment;
    }

    @Produces
    public UserInfo produceUserInfo() {
        // default implementation will load userinfo.properties file on the classpath
        return new DefaultUserInfo(true);
    }


}
