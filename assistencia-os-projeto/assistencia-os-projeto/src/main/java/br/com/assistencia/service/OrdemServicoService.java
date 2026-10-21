package br.com.assistencia.service;
import br.com.assistencia.dto.*;
import br.com.assistencia.entity.*;
import br.com.assistencia.exception.*;
import br.com.assistencia.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.*;
import java.time.format.DateTimeFormatter;
@Service
public class OrdemServicoService {
 private static final char[] HEX="0123456789ABCDEF".toCharArray();
 private final SecureRandom random=new SecureRandom();
 private final ClienteRepository clientes;
 private final EquipamentoRepository equipamentos;
 private final OrdemServicoRepository ordens;
 private final Clock clock;
 public OrdemServicoService(ClienteRepository clientes,EquipamentoRepository equipamentos,OrdemServicoRepository ordens,Clock clock){
  this.clientes=clientes;this.equipamentos=equipamentos;this.ordens=ordens;this.clock=clock;
 }
 @Transactional
 public OrdemResponse abrir(AbrirOrdemRequest request){
  ClienteEntity cliente=clientes.findById(request.clienteId()).orElseThrow(()->new RecursoNaoEncontradoException("Cliente não encontrado."));
  EquipamentoEntity equipamento=equipamentos.findById(request.equipamentoId()).orElseThrow(()->new RecursoNaoEncontradoException("Equipamento não encontrado."));
  LocalDateTime agora=LocalDateTime.now(clock);
  OrdemServicoEntity os=OrdemServicoEntity.abrir(cliente,equipamento,request.descricaoProblema(),gerarProtocolo(agora),agora);
  return OrdemResponse.from(ordens.saveAndFlush(os));
 }
 private String gerarProtocolo(LocalDateTime data){
  String prefixo="OS-"+data.format(DateTimeFormatter.BASIC_ISO_DATE)+"-";
  for(int tentativa=0;tentativa<100;tentativa++){
   StringBuilder s=new StringBuilder(prefixo);
   for(int i=0;i<4;i++)s.append(HEX[random.nextInt(HEX.length)]);
   String protocolo=s.toString();
   if(!ordens.existsByNumeroProtocolo(protocolo))return protocolo;
  }
  throw new IllegalStateException("Não foi possível gerar protocolo disponível.");
 }
}