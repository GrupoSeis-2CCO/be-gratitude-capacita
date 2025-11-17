package servicos.gratitude.be_gratitude_capacita.core.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO de cadastro de usuário com validações básicas de formato.
 * Campos adicionais podem ser adicionados conforme a evolução do domínio.
 */
public record UsuarioCadastroDTO(
        @NotBlank @Size(min = 3, max = 80) String nome,
        @NotBlank @Email String email,
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos") String cpf,
        @NotNull Integer idCargo
) {}
