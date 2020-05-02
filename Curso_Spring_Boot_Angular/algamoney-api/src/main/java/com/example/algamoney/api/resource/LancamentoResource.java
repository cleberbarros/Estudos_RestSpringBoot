package com.example.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	
	@GetMapping
	public List<Lancamento> listar(){
		return lancamentoRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscar(@PathVariable Long codigo) {
		
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
	
	@PostMapping
	public ResponseEntity<Lancamento> criar (@RequestBody Lancamento lancamento, HttpServletResponse response){
	  Lancamento lancamentoSalvo =lancamentoRepository.save(lancamento);
	  
	  publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));  
	  
	  return ResponseEntity.ok(lancamentoSalvo); 
	}
	
	
	
}