package br.com.assistencia.repository;
import br.com.assistencia.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ClienteRepository extends JpaRepository<ClienteEntity,Long>{}