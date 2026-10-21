package br.com.assistencia.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
public record AbrirOrdemRequest(@NotNull @Positive Long clienteId,@NotNull @Positive Long equipamentoId,@NotBlank String descricaoProblema) {}