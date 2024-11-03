package codigo.cadastroqrcode.qrcodecadastro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "qrcode")
@Data
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String valor;

    private String descricao;

    private String status;

    @NotNull
    private LocalDate dataAtualizacao;

    @NotNull
    private LocalDate dataExpiracao;

    @NotNull
    private LocalDate dataVencimento;
}
