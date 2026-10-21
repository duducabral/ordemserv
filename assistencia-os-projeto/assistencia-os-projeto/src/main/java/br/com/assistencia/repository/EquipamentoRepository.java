package br.com.assistencia.repository;
import br.com.assistencia.entity.EquipamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EquipamentoRepository extends JpaRepository<EquipamentoEntity,Long>{}