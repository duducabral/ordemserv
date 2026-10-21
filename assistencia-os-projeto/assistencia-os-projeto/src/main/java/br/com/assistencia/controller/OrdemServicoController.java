package br.com.assistencia.controller;
import br.com.assistencia.dto.*;
import br.com.assistencia.service.OrdemServicoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
@RestController @RequestMapping("/ordens")
public class OrdemServicoController {
 private final OrdemServicoService service;
 public OrdemServicoController(OrdemServicoService service){this.service=service;}
 @PostMapping
 public ResponseEntity<OrdemResponse> abrir(@Valid @RequestBody AbrirOrdemRequest request){
  OrdemResponse response=service.abrir(request);
  return ResponseEntity.created(URI.create("/ordens/"+response.id())).body(response);
 }
}