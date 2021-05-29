package com.dimas.pontointeligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dimas.pontointeligente.api.entities.Lancamento;
import com.dimas.pontointeligente.api.repositories.LancamentoRepository;
import com.dimas.pontointeligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	@Autowired
	private LancamentoRepository lancamentoRepository;

	//public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
	public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, Pageable page) {
		log.info("Buscando lançamentos para o funcionário ID {}", funcionarioId);
		return this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);
	}
	
	@Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscando um lançamento pelo ID {}", id);
		//return Optional.ofNullable(this.lancamentoRepository.getOne(id));
		return Optional.ofNullable( this.getLancamentoById(id) );
	}
	
	@CachePut("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Persistindo o lançamento: {}", lancamento);
		return this.lancamentoRepository.save(lancamento);
	}
	
	public void remover(Long id) {
		log.info("Removendo o lançamento ID {}", id);
		this.lancamentoRepository.deleteById(id);
	}
	
	/*In springBoot 2.0, the return value of findById(id) is Optional<Lancamento>
	 Optional is a wrapper class that wraps the user. If the get() method is returned when returning User, an exception will occur if it is empty. So first deal with the exception*/

	public Lancamento getLancamentoById(Long id) {

		Lancamento lancamento =lancamentoRepository.getOne(id);
	       Optional<Lancamento> optionalLancamento = lancamentoRepository.findById(id);
	       try{
	    	   log.info("tentando getLancamento", id);
	    	   optionalLancamento.get();
	       }catch (Exception ex){
	           return null;
	       }
	       return optionalLancamento.get();
	    }
	 /*This will not cause problems when processing data*/

}
