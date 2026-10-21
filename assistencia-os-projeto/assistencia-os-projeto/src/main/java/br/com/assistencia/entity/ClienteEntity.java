package br.com.assistencia.entity;
import jakarta.persistence.*;
@Entity @Table(name="clientes")
public class ClienteEntity {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String nome;
 @Column(nullable=false) private boolean ativo;
 protected ClienteEntity(){}
 public ClienteEntity(String nome,boolean ativo){this.nome=nome;this.ativo=ativo;}
 public Long getId(){return id;} public boolean isAtivo(){return ativo;}
}