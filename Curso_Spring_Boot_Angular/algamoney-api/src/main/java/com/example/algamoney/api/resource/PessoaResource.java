package com.example.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Pessoa> listar(){
		
		return pessoaRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscarPeloCodigo(@PathVariable Long codigo){
	  Optional<Pessoa> pessoaPesquisada = pessoaRepository.findById(codigo);
	  
	  if(pessoaPesquisada.isPresent()) {
		  return ResponseEntity.ok(pessoaPesquisada.get());
	  }
	  
	  return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Pessoa>criar (@Valid @RequestBody Pessoa pessoa, HttpServletResponse response){
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		
		//aqui estou lançando a acessa que vai ser ouvindo e executada por RecursoCraidoListener
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo())); 
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
}
