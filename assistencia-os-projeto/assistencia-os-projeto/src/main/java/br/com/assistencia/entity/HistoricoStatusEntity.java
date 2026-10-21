package br.com.assistencia.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="historico_status")
public class HistoricoStatusEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="ordem_id",nullable=false) private OrdemServicoEntity ordem;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private StatusOrdem status;
 @Column(nullable=false) private String responsavel;
 @Column(nullable=false) private String observacao;
 @Column(nullable=false) private LocalDateTime dataRegistro;
 protected HistoricoStatusEntity(){}
 public HistoricoStatusEntity(OrdemServicoEntity ordem,StatusOrdem status,String responsavel,String observacao,LocalDateTime dataRegistro){
  this.ordem=ordem;this.status=status;this.responsavel=responsavel;this.observacao=observacao;this.dataRegistro=dataRegistro;
 }
 public StatusOrdem getStatus(){return status;} public String getResponsavel(){return responsavel;}
}