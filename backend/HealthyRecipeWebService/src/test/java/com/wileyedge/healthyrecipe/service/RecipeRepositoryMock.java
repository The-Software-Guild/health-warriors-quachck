package com.wileyedge.healthyrecipe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import com.wileyedge.healthyrecipe.dao.RecipeRepository;
import com.wileyedge.healthyrecipe.model.HealthCategory;
import com.wileyedge.healthyrecipe.model.Recipe;
import com.wileyedge.healthyrecipe.model.User;

public class RecipeRepositoryMock implements RecipeRepository {

	private List<Recipe> recipes = new ArrayList<>();

	@Override
	public <S extends Recipe> S save(S entity) {
		recipes.add(entity);
		return entity;
	}

	@Override
	public List<Recipe> findAll() {
		return null;
	}

	@Override
	public List<Recipe> findAll(Sort sort) {
		return null;
	}

	@Override
	public List<Recipe> findAllById(Iterable<Long> ids) {
		return null;
	}

	@Override
	public <S extends Recipe> List<S> saveAll(Iterable<S> entities) {
		return null;
	}

	@Override
	public void flush() {
	}

	@Override
	public <S extends Recipe> S saveAndFlush(S entity) {
		return null;
	}

	@Override
	public <S extends Recipe> List<S> saveAllAndFlush(Iterable<S> entities) {
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Recipe> entities) {
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
	}

	@Override
	public void deleteAllInBatch() {
	}

	@Override
	public Recipe getOne(Long id) {
		return null;
	}

	@Override
	public Recipe getById(Long id) {
		return null;
	}

	@Override
	public Recipe getReferenceById(Long id) {
		return null;
	}

	@Override
	public <S extends Recipe> List<S> findAll(Example<S> example) {
		return null;
	}

	@Override
	public <S extends Recipe> List<S> findAll(Example<S> example, Sort sort) {
		return null;
	}

	@Override
	public Page<Recipe> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public Optional<Recipe> findById(Long id) {
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		return false;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(Long id) {
	}

	@Override
	public void delete(Recipe entity) {
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
	}

	@Override
	public void deleteAll(Iterable<? extends Recipe> entities) {
	}

	@Override
	public void deleteAll() {
	}

	@Override
	public <S extends Recipe> Optional<S> findOne(Example<S> example) {
		return null;
	}

	@Override
	public <S extends Recipe> Page<S> findAll(Example<S> example, Pageable pageable) {
		return null;
	}

	@Override
	public <S extends Recipe> long count(Example<S> example) {
		return 0;
	}

	@Override
	public <S extends Recipe> boolean exists(Example<S> example) {
		return false;
	}

	@Override
	public <S extends Recipe, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
		return null;
	}

	@Override
	public List<Recipe> findByUserId(long userId) {
		return null;
	}

	@Override
	public List<Recipe> findBySuitableForIn(List<HealthCategory> healthCategories) {
		return null;
	}

}

