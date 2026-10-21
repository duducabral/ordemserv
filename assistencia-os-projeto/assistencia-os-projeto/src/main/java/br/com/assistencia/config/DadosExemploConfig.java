package br.com.assistencia.config;
import br.com.assistencia.entity.*;
import br.com.assistencia.repository.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;
@Configuration public class DadosExemploConfig {
 @Bean CommandLineRunner dados(ClienteRepository clientes,EquipamentoRepository equipamentos){
  return args->{
   if(clientes.count()==0){
    ClienteEntity ativo=clientes.save(new ClienteEntity("Maria Silva",true));
    ClienteEntity inativo=clientes.save(new ClienteEntity("João Souza",false));
    ClienteEntity outro=clientes.save(new ClienteEntity("Ana Lima",true));
    equipamentos.save(new EquipamentoEntity("Notebook Dell",ativo));
    equipamentos.save(new EquipamentoEntity("Celular Samsung",outro));
    equipamentos.save(new EquipamentoEntity("Impressora",inativo));
   }
  };
 }
}