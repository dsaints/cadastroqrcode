package codigo.cadastroqrcode.qrcodecadastro.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "qrcode")
@Data
public class QrCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String valor;
    private String descricao;
    private String status;
    private LocalDate dataAtualizacao;
    private LocalDate dataExpiracao;
    private LocalDate dataVencimento;
}
