package br.com.assistencia;
import br.com.assistencia.entity.*;
import br.com.assistencia.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
@SpringBootTest @AutoConfigureMockMvc
class OrdemServicoIntegrationTest {
 @Autowired MockMvc mvc;
 @Autowired ClienteRepository clientes;
 @Autowired EquipamentoRepository equipamentos;
 @Autowired OrdemServicoRepository ordens;
 @Autowired HistoricoStatusRepository historicos;
 ClienteEntity ativo,inativo,outro;
 EquipamentoEntity equipamento;
 @BeforeEach void preparar(){
  historicos.deleteAll();ordens.deleteAll();equipamentos.deleteAll();clientes.deleteAll();
  ativo=clientes.save(new ClienteEntity("Cliente ativo",true));
  inativo=clientes.save(new ClienteEntity("Cliente inativo",false));
  outro=clientes.save(new ClienteEntity("Outro cliente",true));
  equipamento=equipamentos.save(new EquipamentoEntity("Notebook",ativo));
 }
 String json(Long c,Long e,String descricao){
  return "{\"clienteId\":"+c+",\"equipamentoId\":"+e+",\"descricaoProblema\":\""+descricao+"\"}";
 }
 @Test void sucesso201ComHistorico() throws Exception {
  mvc.perform(post("/ordens").contentType(MediaType.APPLICATION_JSON).content(json(ativo.getId(),equipamento.getId(),"Não liga")))
   .andExpect(status().isCreated()).andExpect(header().exists("Location"))
   .andExpect(jsonPath("$.id").isNumber())
   .andExpect(jsonPath("$.numeroProtocolo",matchesPattern("OS-[0-9]{8}-[0-9A-F]{4}")))
   .andExpect(jsonPath("$.status").value("RECEBIDA"))
   .andExpect(jsonPath("$.dataAbertura").exists());
  Assertions.assertEquals(1,historicos.count());
  var h=historicos.findAll().get(0);
  Assertions.assertEquals(StatusOrdem.RECEBIDA,h.getStatus());
  Assertions.assertEquals("SISTEMA",h.getResponsavel());
 }
 @Test void camposInvalidos400() throws Exception {
  mvc.perform(post("/ordens").contentType(MediaType.APPLICATION_JSON).content("{\"clienteId\":null,\"equipamentoId\":null,\"descricaoProblema\":\"  \"}"))
   .andExpect(status().isBadRequest()).andExpect(jsonPath("$.camposInvalidos.clienteId").exists())
   .andExpect(jsonPath("$.camposInvalidos.equipamentoId").exists())
   .andExpect(jsonPath("$.camposInvalidos.descricaoProblema").exists());
 }
 @Test void clienteInexistente404() throws Exception {
  mvc.perform(post("/ordens").contentType(MediaType.APPLICATION_JSON).content(json(999999L,equipamento.getId(),"Defeito")))
   .andExpect(status().isNotFound()).andExpect(jsonPath("$.detail").value("Cliente não encontrado."));
 }
 @Test void equipamentoInexistente404() throws Exception {
  mvc.perform(post("/ordens").contentType(MediaType.APPLICATION_JSON).content(json(ativo.getId(),999999L,"Defeito")))
   .andExpect(status().isNotFound()).andExpect(jsonPath("$.detail").value("Equipamento não encontrado."));
 }
 @Test void clienteInativo422() throws Exception {
  var eq=equipamentos.save(new EquipamentoEntity("Tablet",inativo));
  mvc.perform(post("/ordens").contentType(MediaType.APPLICATION_JSON).content(json(inativo.getId(),eq.getId(),"Defeito")))
   .andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.detail").value("Não é permitido abrir Ordem de Serviço para um cliente inativo."));
 }
 @Test void equipamentoDeOutroCliente422() throws Exception {
  mvc.perform(post("/ordens").contentType(MediaType.APPLICATION_JSON).content(json(outro.getId(),equipamento.getId(),"Defeito")))
   .andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.detail").value("O equipamento informado não pertence ao cliente indicado."));
 }
 @Test void protocolosDiferentes() throws Exception {
  for(int i=0;i<2;i++)mvc.perform(post("/ordens").contentType(MediaType.APPLICATION_JSON).content(json(ativo.getId(),equipamento.getId(),"Defeito"))).andExpect(status().isCreated());
  var lista=ordens.findAll();
  Assertions.assertNotEquals(lista.get(0).getNumeroProtocolo(),lista.get(1).getNumeroProtocolo());
 }
}