package br.com.assistencia.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity @Table(name="ordens_servico",uniqueConstraints=@UniqueConstraint(name="uk_os_protocolo",columnNames="numero_protocolo"))
public class OrdemServicoEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="numero_protocolo",nullable=false,unique=true,length=16,updatable=false) private String numeroProtocolo;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private ClienteEntity cliente;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private EquipamentoEntity equipamento;
 @Column(nullable=false,length=2000) private String descricaoProblema;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private StatusOrdem status;
 @Column(nullable=false,updatable=false) private LocalDateTime dataAbertura;
 @OneToMany(mappedBy="ordem",cascade=CascadeType.ALL,orphanRemoval=true) private List<HistoricoStatusEntity> historicos=new ArrayList<>();
 protected OrdemServicoEntity(){}
 public static OrdemServicoEntity abrir(ClienteEntity cliente,EquipamentoEntity equipamento,String descricao,String protocolo,LocalDateTime data){
  if(!cliente.isAtivo()) throw new br.com.assistencia.exception.RegraNegocioException("Não é permitido abrir Ordem de Serviço para um cliente inativo.");
  if(!equipamento.getCliente().getId().equals(cliente.getId())) throw new br.com.assistencia.exception.RegraNegocioException("O equipamento informado não pertence ao cliente indicado.");
  OrdemServicoEntity os=new OrdemServicoEntity();
  os.cliente=cliente;os.equipamento=equipamento;os.descricaoProblema=descricao;os.numeroProtocolo=protocolo;
  os.status=StatusOrdem.RECEBIDA;os.dataAbertura=data;
  os.historicos.add(new HistoricoStatusEntity(os,StatusOrdem.RECEBIDA,"SISTEMA","Abertura da Ordem de Serviço.",data));
  return os;
 }
 public Long getId(){return id;} public String getNumeroProtocolo(){return numeroProtocolo;}
 public StatusOrdem getStatus(){return status;} public LocalDateTime getDataAbertura(){return dataAbertura;}
 public List<HistoricoStatusEntity> getHistoricos(){return historicos;}
}