package br.com.assistencia.entity;
import jakarta.persistence.*;
@Entity @Table(name="equipamentos")
public class EquipamentoEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="cliente_id",nullable=false) private ClienteEntity cliente;
 @Column(nullable=false) private String nome;
 protected EquipamentoEntity(){}
 public EquipamentoEntity(String nome,ClienteEntity cliente){this.nome=nome;this.cliente=cliente;}
 public Long getId(){return id;} public ClienteEntity getCliente(){return cliente;}
}