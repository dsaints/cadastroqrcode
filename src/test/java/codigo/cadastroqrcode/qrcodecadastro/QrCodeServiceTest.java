package codigo.cadastroqrcode.qrcodecadastro;

import codigo.cadastroqrcode.qrcodecadastro.entity.QrCode;
import codigo.cadastroqrcode.qrcodecadastro.repository.QrCodeRepository;
import codigo.cadastroqrcode.qrcodecadastro.service.QrCodeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Import(QrCodeService.class)
@DirtiesContext
public class QrCodeServiceTest {

    @Mock
    private QrCodeRepository qrCodeRepository;

    @InjectMocks
    private QrCodeService qrCodeService;

    @Test
    void testGerandoDadosQrCode() {
        String valor = "100.00";
        String descricao = "Test QR Code";
        String status = "Ativo";
        LocalDate dataAtualizacao = LocalDate.now();
        LocalDate dataExpiracao = LocalDate.now().plusDays(10);

        QrCode qrCode = new QrCode();
        qrCode.setValor(valor);
        qrCode.setDescricao(descricao);
        qrCode.setStatus(status);
        qrCode.setDataAtualizacao(dataAtualizacao);
        qrCode.setDataExpiracao(dataExpiracao);

        when(qrCodeRepository.save(any(QrCode.class))).thenAnswer(invocation -> {
            QrCode savedQrCode = invocation.getArgument(0);
            savedQrCode.setId(1L);
            return savedQrCode;
        });

        when(qrCodeRepository.findById(1L)).thenReturn(Optional.of(qrCode));

        qrCode = qrCodeRepository.save(qrCode);
        Long id = qrCode.getId();

        assertDoesNotThrow(() -> qrCodeService.gerarDadosQrCode(id, valor, descricao, status, dataAtualizacao, dataExpiracao));

        QrCode dadosQrCode = qrCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("QrCode não encontrado com o id " + id));
        assertNotNull(dadosQrCode, "QrCode não deve ser nulo");
        assertEquals(id, dadosQrCode.getId(), "IDs devem ser iguais");
        assertEquals(valor, dadosQrCode.getValor(), "Valores devem ser iguais");
        assertEquals(descricao, dadosQrCode.getDescricao(), "Descrições devem ser iguais");
        assertEquals(status, dadosQrCode.getStatus(), "Status devem ser iguais");
        assertEquals(dataAtualizacao, dadosQrCode.getDataAtualizacao(), "Datas de atualização devem ser iguais");
        assertEquals(dataExpiracao, dadosQrCode.getDataExpiracao(), "Datas de expiração devem ser iguais");
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

        when(qrCodeRepository.save(any(QrCode.class))).thenAnswer(invocation -> {
            QrCode savedQrCode = invocation.getArgument(0);
            savedQrCode.setId(1L);
            return savedQrCode;
        });

        when(qrCodeRepository.findById(1L)).thenReturn(Optional.of(qrCode));

        qrCode = qrCodeRepository.save(qrCode);
        Long idGenerated = qrCode.getId();

        assertDoesNotThrow(() -> qrCodeService.gerarDadosQrCodeVencimento(idGenerated, valor, descricao, status, dataAtualizacao, dataExpiracao, dataVencimento));

        QrCode dadosQrCode = qrCodeRepository.findById(idGenerated).orElseThrow(() -> new EntityNotFoundException("QrCode não encontrado com o id " + idGenerated));
        assertNotNull(dadosQrCode, "QrCode não deve ser nulo");
        assertEquals(idGenerated, dadosQrCode.getId(), "IDs devem ser iguais");
        assertEquals(valor, dadosQrCode.getValor(), "Valores devem ser iguais");
        assertEquals(descricao, dadosQrCode.getDescricao(), "Descrições devem ser iguais");
        assertEquals(status, dadosQrCode.getStatus(), "Status devem ser iguais");
        assertEquals(dataAtualizacao, dadosQrCode.getDataAtualizacao(), "Datas de atualização devem ser iguais");
        assertEquals(dataExpiracao, dadosQrCode.getDataExpiracao(), "Datas de expiração devem ser iguais");
        assertEquals(dataVencimento, dadosQrCode.getDataVencimento(), "Datas de vencimento devem ser iguais");
    }

    @Test
    void testStatusOpen() {
        String valor = "100.00";
        String descricao = "Test QR Code";
        String status = null;
        LocalDate dataAtualizacao = LocalDate.now();
        LocalDate dataExpiracao = LocalDate.now().plusDays(10);
        LocalDate dataVencimento = LocalDate.now().plusDays(5);

        QrCode qrCode = new QrCode();
        qrCode.setValor(valor);
        qrCode.setDescricao(descricao);
        qrCode.setStatus("OPEN");
        qrCode.setDataAtualizacao(dataAtualizacao);
        qrCode.setDataExpiracao(dataExpiracao);
        qrCode.setDataVencimento(dataVencimento);

        when(qrCodeRepository.save(any(QrCode.class))).thenAnswer(invocation -> {
            QrCode savedQrCode = invocation.getArgument(0);
            savedQrCode.setId(1L);
            return savedQrCode;
        });

        when(qrCodeRepository.findById(1L)).thenReturn(Optional.of(qrCode));

        qrCode = qrCodeRepository.save(qrCode);
        Long id = qrCode.getId();

        qrCodeService.gerarDadosQrCodeVencimento(id, valor, descricao, status, dataAtualizacao, dataExpiracao, dataVencimento);

        QrCode dadosQrCode = qrCodeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("QrCode não encontrado com o id " + id));
        assertNotNull(dadosQrCode, "QrCode não deve ser nulo");
        assertEquals("OPEN", dadosQrCode.getStatus(), "Status deve ser 'OPEN'");
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

        assertThrows(IllegalArgumentException.class, () -> qrCodeService.validarDadosQrCode(id, valor, dataAtualizacao, dataExpiracao), "Deve lançar IllegalArgumentException");
    }

    @Test
    void testValidarDataVencimento() {
        LocalDate dataVencimento = LocalDate.now().plusDays(5);

        assertDoesNotThrow(() -> qrCodeService.validarDataVencimento(dataVencimento));
    }

    @Test
    void testValidarDataVencimentoInvalid() {
        LocalDate dataVencimento = LocalDate.now().minusDays(1);

        assertThrows(IllegalArgumentException.class, () -> qrCodeService.validarDataVencimento(dataVencimento), "Deve lançar IllegalArgumentException");
    }
}