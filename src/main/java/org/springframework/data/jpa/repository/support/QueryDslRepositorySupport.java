/*
 * Copyright 2011 the original author or authors.
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
package org.springframework.data.jpa.repository.support;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.mysema.query.dml.DeleteClause;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.PathBuilderFactory;

/**
 * Base class for implementing repositories using QueryDsl library.
 * 
 * @author Oliver Gierke
 */
@Repository
public abstract class QueryDslRepositorySupport {

	private EntityManager entityManager;
	private PathBuilderFactory builderFactory = new PathBuilderFactory();

	/**
	 * Setter to inject {@link EntityManager}.
	 * 
	 * @param entityManager must not be {@literal null}
	 */
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {

		Assert.notNull(entityManager);
		this.entityManager = entityManager;
	}

	/**
	 * Callback to verify configuration. Used by containers.
	 */
	@PostConstruct
	public void validate() {
		Assert.notNull(entityManager, "EntityManager must not be null!");
	}

	/**
	 * Returns the {@link EntityManager}.
	 * 
	 * @return the entityManager
	 */
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Returns a fresh {@link JPQLQuery}.
	 * 
	 * @return
	 */
	protected JPQLQuery from(EntityPath<?>... paths) {

		return new JPAQuery(entityManager).from(paths);
	}

	/**
	 * Returns a fresh {@link DeleteClause}.
	 * 
	 * @param path
	 * @return
	 */
	protected DeleteClause<JPADeleteClause> delete(EntityPath<?> path) {

		return new JPADeleteClause(entityManager, path);
	}

	/**
	 * Returns a fresh {@link UpdateClause}.
	 * 
	 * @param path
	 * @return
	 */
	protected UpdateClause<JPAUpdateClause> update(EntityPath<?> path) {

		return new JPAUpdateClause(entityManager, path);
	}

	/**
	 * Returns a {@link PathBuilder} for the given type.
	 * 
	 * @param <T>
	 * @param type
	 * @return
	 */
	protected <T> PathBuilder<T> getBuilder(Class<T> type) {

		return builderFactory.create(type);
	}
}
