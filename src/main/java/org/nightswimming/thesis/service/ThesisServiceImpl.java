package org.nightswimming.thesis.service;

import java.util.List;
import java.util.Optional;

import org.nightswimming.thesis.domain.Thesis;
import org.nightswimming.thesis.domain.ThesisTarget;
import org.nightswimming.thesis.domain.Thesis.Country;
import org.nightswimming.thesis.repository.ThesisRepository;
import org.nightswimming.thesis.repository.ThesisTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, timeout = 10)
public class ThesisServiceImpl implements ThesisService {
   
	@Value("${rest.pagination_size}")
	private int resultsPerPage;	
	@Autowired private ThesisRepository thesisRepo;
	@Autowired private ThesisTargetRepository sTargetsRepo;

	@Override
    public Optional<Thesis> findThesisById(long id) {
        return Optional.ofNullable(thesisRepo.findOne(id));
    }
  
	@Override
	public List<Thesis> findThesisBySubject(int subject, Optional<Country> country, int page, Sort sorter){
		PageRequest pageRequest = new PageRequest(page,resultsPerPage,sorter);
		return  country.isPresent()? thesisRepo.findBySubjectAndCountry(subject,country.get(),pageRequest).getContent()
								   : thesisRepo.findBySubject(subject,pageRequest).getContent();
	}
	
	@Override
	public List<Thesis> findThesisByTarget(ThesisTarget target, Optional<Country> country, int page, Sort sorter){
		PageRequest pageRequest = new PageRequest(page,resultsPerPage,sorter);	
		return  country.isPresent()? thesisRepo.findByTargetAndCountry(target,country.get(),pageRequest).getContent()
								   : thesisRepo.findByTarget(target, pageRequest).getContent();

	}
}