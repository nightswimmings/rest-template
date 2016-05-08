package org.nightswimming.thesis.service;

import java.util.List;
import java.util.Optional;

import org.nightswimming.thesis.domain.Thesis;
import org.nightswimming.thesis.domain.ThesisTarget;
import org.nightswimming.thesis.domain.Thesis.Country;
import org.springframework.data.domain.Sort;

public interface ThesisService {
	
	Optional<Thesis> findThesisById(long id);
	List<Thesis> findThesisBySubject(int subject, Optional<Country> country, int page, Sort sorter);
	List<Thesis> findThesisByTarget(ThesisTarget target, Optional<Country> country, int page, Sort sorter);
	//boolean createThesis(Thesis thesis);
	//boolean deleteThesisById(long id);
}
