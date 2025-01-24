package com.eggzin.javer_service.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eggzin.javer_service.entity.Cliente;
import com.eggzin.javer_service.proxy.ClienteProxy;
import com.eggzin.javer_service.util.ScoreUtils;
import com.eggzin.javer_service.web.dto.ClienteCreateDto;
import com.eggzin.javer_service.web.dto.ClienteEditDto;
import com.eggzin.javer_service.web.dto.ClienteResponseDto;
import com.eggzin.javer_service.web.dto.ScoreResponseDto;
import com.eggzin.javer_service.web.dto.mapper.ClienteMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Javer (General) Controller", description = "Expõe os endpoints do recurso 'clientes' vindos do serviço 'Cliente-Service'")
@RequiredArgsConstructor
@RestController
@RequestMapping("javer-service/clientes")
public class JaverController {
	
	@Autowired
	private ClienteProxy proxy;
	
	@Autowired
	private Environment environment;
	
	
	@Operation(summary = "Mandar uma requisição ao serviço de Clientes para criar um cliente")
	@PostMapping
	public ResponseEntity<ClienteResponseDto> create(@RequestHeader("Authorization") String token, @Valid @RequestBody ClienteCreateDto dto){
		
		return proxy.create(token, dto);
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization", token);
		 * HttpEntity<?> entityReq = new HttpEntity<>(dto, headers);
		 * 
		 * return new RestTemplate() .exchange("http://localhost:8000/clientes",
		 * HttpMethod.POST, entityReq, ClienteResponseDto.class);
		 */
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDto> getById(@RequestHeader("Authorization") String token, @PathVariable Long id){
		
		return proxy.getById(token, id);
		
		/*
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		HttpEntity<?> entityReq = new HttpEntity<>(headers);
		
		Long param = id;
		
		return new RestTemplate()
				.exchange("http://localhost:8000/clientes/{id}",HttpMethod.GET, entityReq, ClienteResponseDto.class, param);
		 */
	
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<ClienteResponseDto> edit(@RequestHeader("Authorization") String token, @PathVariable Long id, @Valid @RequestBody ClienteEditDto dto){
		
		return proxy.edit(token, id, dto);
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization", token);
		 * HttpEntity<?> entityReq = new HttpEntity<>(dto, headers);
		 * 
		 * Long param = id;
		 * 
		 * RestTemplate restTemplate = new RestTemplate(new
		 * HttpComponentsClientHttpRequestFactory());
		 * 
		 * return restTemplate .exchange("http://localhost:8000/clientes/{id}",
		 * HttpMethod.PATCH, entityReq, ClienteResponseDto.class, param);
		 */
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@RequestHeader("Authorization") String token, @PathVariable Long id){
		return proxy.delete(token, id);
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization", token);
		 * HttpEntity<?> entityReq = new HttpEntity<>(headers);
		 * 
		 * Long param = id;
		 * 
		 * new RestTemplate()
		 * .exchange("http://localhost:8000/clientes/{id}",HttpMethod.DELETE, entityReq,
		 * Void.class, param);
		 * 
		 * return ResponseEntity.noContent().build();
		 */
	}
	
	@GetMapping("/{id}/score")
	public ResponseEntity<ScoreResponseDto> calcularScore(@RequestHeader("Authorization") String token, @PathVariable Long id){
		
		ClienteResponseDto cliResDto = proxy.getById(token, id).getBody();
		Cliente cliente = ClienteMapper.toCliente(cliResDto);
		ScoreResponseDto dto = new ScoreResponseDto();
		dto.setScoreCredito(ScoreUtils.calcScore(cliente.getSaldo()));
		
		return ResponseEntity.ok(dto);
		
		/*
		 * HttpHeaders headers = new HttpHeaders(); headers.set("Authorization", token);
		 * HttpEntity<?> entityReq = new HttpEntity<>(headers);
		 * 
		 * Long param = id;
		 * 
		 * ClienteResponseDto cliResDto = new RestTemplate()
		 * .exchange("http://localhost:8000/clientes/{id}", HttpMethod.GET, entityReq,
		 * ClienteResponseDto.class, param).getBody();
		 * 
		 * Cliente cliente = ClienteMapper.toCliente(cliResDto); ScoreResponseDto dto =
		 * new ScoreResponseDto();
		 * dto.setScoreCredito(ScoreUtils.calcScore(cliente.getSaldo()));
		 * 
		 * return ResponseEntity.ok(dto);
		 */
		
	}

}
