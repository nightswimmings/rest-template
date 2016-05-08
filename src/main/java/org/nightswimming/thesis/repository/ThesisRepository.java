package org.nightswimming.thesis.repository;

import java.util.Optional;

import org.nightswimming.thesis.domain.Thesis;
import org.nightswimming.thesis.domain.ThesisTarget;
import org.nightswimming.thesis.domain.Thesis.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ThesisRepository extends JpaRepository<Thesis,Long> {
	public static final String TARGETS_QUERY = "SELECT s FROM Thesis s INNER JOIN s.target t WHERE "
									 + "t.gender = :#{#target.gender} AND "
									 + "t.ageRange.minValue >= :#{#target.ageRange.minValue} AND "
									 + "t.ageRange.maxValue <= :#{#target.ageRange.maxValue} AND " //This could be an OR perhaps
									 + "t.income.currency = :#{#target.income.currency} AND "
									 + "t.income.range.minValue >= :#{#target.income.range.minValue} AND "
									 + "t.income.range.maxValue <= :#{#target.income.range.maxValue}";
									 
	Optional<Thesis> findOneById(Long id);
	Page<Thesis> findBySubject(int subject, Pageable pageRequest);
	Page<Thesis> findBySubjectAndCountry(int subject, Country country, Pageable pageRequest);
	
	@Query(TARGETS_QUERY)
	Page<Thesis> findByTarget(@Param("target") ThesisTarget target, Pageable pageable);
	
    @Query(TARGETS_QUERY + " AND s.country = :#{#country}")
	Page<Thesis> findByTargetAndCountry(@Param("target") ThesisTarget target, @Param("country") Country country, Pageable pageable);
}