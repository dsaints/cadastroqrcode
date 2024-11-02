package codigo.cadastroqrcode.qrcodecadastro;

import codigo.cadastroqrcode.qrcodecadastro.entity.QrCode;
import codigo.cadastroqrcode.qrcodecadastro.repository.QrCodeRepository;
import codigo.cadastroqrcode.qrcodecadastro.service.QrCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class QrCodeServiceTest {

    @Mock
    private QrCodeRepository qrCodeRepository;

    @InjectMocks
    private QrCodeService qrCodeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGerandoDadosQrCode() {
        Long id = 1L;
        String valor = "100.00";
        String descricao = "Test QR Code";
        String status = "Ativo";
        LocalDate dataAtualizacao = LocalDate.now();
        LocalDate dataExpiracao = LocalDate.now().plusDays(10);

        QrCode qrCode = new QrCode();
        qrCode.setId(id);
        qrCode.setValor(valor);
        qrCode.setDescricao(descricao);
        qrCode.setStatus(status);
        qrCode.setDataAtualizacao(dataAtualizacao);
        qrCode.setDataExpiracao(dataExpiracao);

        when(qrCodeRepository.save(any(QrCode.class))).thenReturn(qrCode);

        assertDoesNotThrow(() -> qrCodeService.gerarDadosQrCode(id, valor, descricao, status, dataAtualizacao, dataExpiracao));

        verify(qrCodeRepository, times(1)).save(any(QrCode.class));
    }

    @Test
    void testGerandoDadosQrCodeVencimento() {
        Long id = 1L;
        String valor = "100.00";
        String descricao = "Test QR Code";
        String status = "Ativo";
        LocalDate dataAtualizacao = LocalDate.now();
        LocalDate dataExpiracao = LocalDate.now().plusDays(10);
        LocalDate dataVencimento = LocalDate.now().plusDays(5);

        QrCode qrCode = new QrCode();
        qrCode.setId(id);
        qrCode.setValor(valor);
        qrCode.setDescricao(descricao);
        qrCode.setStatus(status);
        qrCode.setDataAtualizacao(dataAtualizacao);
        qrCode.setDataExpiracao(dataExpiracao);
        qrCode.setDataVencimento(dataVencimento);

        when(qrCodeRepository.save(any(QrCode.class))).thenReturn(qrCode);

        assertDoesNotThrow(() -> qrCodeService.gerarDadosQrCodeVencimento(id, valor, descricao, status, dataAtualizacao, dataExpiracao, dataVencimento));

        verify(qrCodeRepository, times(1)).save(any(QrCode.class));
    }

    @Test
    void testStatusOpen() {
        Long id = 1L;
        String valor = "100.00";
        String descricao = "Test QR Code";
        String status = null;
        LocalDate dataAtualizacao = LocalDate.now();
        LocalDate dataExpiracao = LocalDate.now().plusDays(10);
        LocalDate dataVencimento = LocalDate.now().plusDays(5);

        QrCode qrCode = new QrCode();
        qrCode.setId(id);
        qrCode.setValor(valor);
        qrCode.setDescricao(descricao);
        qrCode.setStatus("OPEN");
        qrCode.setDataAtualizacao(dataAtualizacao);
        qrCode.setDataExpiracao(dataExpiracao);
        qrCode.setDataVencimento(dataVencimento);

        when(qrCodeRepository.save(any(QrCode.class))).thenReturn(qrCode);

        qrCodeService.gerarDadosQrCodeVencimento(id, valor, descricao, status, dataAtualizacao, dataExpiracao, dataVencimento);

        verify(qrCodeRepository, times(1)).save(argThat(savedQrCode -> "OPEN".equals(savedQrCode.getStatus())));
    }

    @Test
    void testValidarDadosQrCode() {
        Long id = 1L;
        String valor = "100.00";
        LocalDate dataAtualizacao = LocalDate.now();
        LocalDate dataExpiracao = LocalDate.now().plusDays(10);

        assertDoesNotThrow(() -> qrCodeService.validarDadosQrCode(id, valor, dataAtualizacao, dataExpiracao));
    }

    @Test
    void testValidarDadosQrCodeInvalid() {
        Long id = null;
        String valor = "0.00";
        LocalDate dataAtualizacao = null;
        LocalDate dataExpiracao = null;

        assertThrows(IllegalArgumentException.class, () -> qrCodeService.validarDadosQrCode(id, valor, dataAtualizacao, dataExpiracao));
    }

    @Test
    void testValidarDataVencimento() {
        LocalDate dataVencimento = LocalDate.now().plusDays(5);

        assertDoesNotThrow(() -> qrCodeService.validarDataVencimento(dataVencimento));
    }

    @Test
    void testValidarDataVencimentoInvalid() {
        LocalDate dataVencimento = LocalDate.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> qrCodeService.validarDataVencimento(dataVencimento));
    }
}
