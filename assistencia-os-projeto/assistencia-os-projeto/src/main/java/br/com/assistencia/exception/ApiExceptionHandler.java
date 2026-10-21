package br.com.assistencia.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
 private ProblemDetail problem(HttpStatus status,String title,String detail){
  ProblemDetail p=ProblemDetail.forStatusAndDetail(status,detail);
  p.setTitle(title);p.setType(URI.create("about:blank"));return p;
 }
 @ExceptionHandler(RegraNegocioException.class)
 public ResponseEntity<ProblemDetail> regra(RegraNegocioException e){
  return ResponseEntity.status(422).body(problem(HttpStatus.UNPROCESSABLE_ENTITY,"Regra de negócio violada",e.getMessage()));
 }
 @ExceptionHandler(RecursoNaoEncontradoException.class)
 public ResponseEntity<ProblemDetail> naoEncontrado(RecursoNaoEncontradoException e){
  return ResponseEntity.status(404).body(problem(HttpStatus.NOT_FOUND,"Recurso não encontrado",e.getMessage()));
 }
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ProblemDetail> validacao(MethodArgumentNotValidException e){
  Map<String,String> campos=new LinkedHashMap<>();
  e.getBindingResult().getFieldErrors().forEach(f->campos.putIfAbsent(f.getField(),f.getDefaultMessage()));
  ProblemDetail p=problem(HttpStatus.BAD_REQUEST,"Dados inválidos","Verifique os campos informados.");
  p.setProperty("camposInvalidos",campos);
  return ResponseEntity.badRequest().body(p);
 }
 @ExceptionHandler(HttpMessageNotReadableException.class)
 public ResponseEntity<ProblemDetail> json(HttpMessageNotReadableException e){
  return ResponseEntity.badRequest().body(problem(HttpStatus.BAD_REQUEST,"JSON inválido","Corpo da requisição ausente ou malformado."));
 }
}