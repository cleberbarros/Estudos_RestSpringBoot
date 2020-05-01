package com.example.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@GetMapping
	private List<Lancamento> listar(){
		return lancamentoRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	private ResponseEntity<Lancamento> buscar(@PathVariable Long codigo) {
		
	Optional<Lancamento> lancamentoPesquisado = lancamentoRepository.findById(codigo);
	
//	if(lancamentoPesquisado.isPresent()) {
//		return ResponseEntity.ok(lancamentoPesquisado.get());
//	}
//	
//	return ResponseEntity.notFound().build();
//	
	
	if(!lancamentoPesquisado.isPresent()) {
		throw new EmptyResultDataAccessException(1);
	}
	
	return ResponseEntity.ok(lancamentoPesquisado.get());
	
		
	}
}
