package br.com.assistencia.dto;
import br.com.assistencia.entity.OrdemServicoEntity;
import br.com.assistencia.entity.StatusOrdem;
import java.time.LocalDateTime;
public record OrdemResponse(Long id,String numeroProtocolo,StatusOrdem status,LocalDateTime dataAbertura){
 public static OrdemResponse from(OrdemServicoEntity os){return new OrdemResponse(os.getId(),os.getNumeroProtocolo(),os.getStatus(),os.getDataAbertura());}
}