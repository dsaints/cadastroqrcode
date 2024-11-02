package codigo.cadastroqrcode.qrcodecadastro.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class QrCodeDTO {
    private Long id;
    private String valor;
    private String descricao;
    private String status;
    private LocalDate dataAtualizacao;
    private LocalDate dataExpiracao;
    private LocalDate dataVencimento;
}