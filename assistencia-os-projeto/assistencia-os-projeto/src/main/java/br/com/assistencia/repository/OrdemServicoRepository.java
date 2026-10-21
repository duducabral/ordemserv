package br.com.assistencia.repository;
import br.com.assistencia.entity.OrdemServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrdemServicoRepository extends JpaRepository<OrdemServicoEntity,Long>{
 boolean existsByNumeroProtocolo(String numeroProtocolo);
}