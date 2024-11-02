package codigo.cadastroqrcode.qrcodecadastro.service;

import codigo.cadastroqrcode.qrcodecadastro.entity.QrCode;
import codigo.cadastroqrcode.qrcodecadastro.repository.QrCodeRepository;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Classe de serviço para cadastrar e gerar QR Code.
 * Author: Davi Oliveira Santos
 */
@Service
@Slf4j
public class QrCodeService {

    private static final String DESTINO_ARQUIVO = "C:\\Users\\dsain\\Documents";

    private static final String DATA_FORMATADA = "yyyyMMddhhmmss";

    private final QrCodeRepository qrCodeRepository;

    public QrCodeService(QrCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    /**
     * Gera dados do QR Code e salva no repositório.
     *
     * @param id o ID do QR Code
     * @param valor o valor do QR Code
     * @param descricao a descrição do QR Code
     * @param status o status do QR Code
     * @param dataAtualizacao a data de atualização do QR Code
     * @param dataExpiracao a data de expiração do QR Code
     */
    public void gerarDadosQrCode(Long id, String valor, String descricao, String status, LocalDate dataAtualizacao, LocalDate dataExpiracao) {
        validarDadosQrCode(id, valor, dataAtualizacao, dataExpiracao);

        QrCode qrCode = persistirDados(id, valor, descricao, status, dataAtualizacao, dataExpiracao, null);
        qrCodeRepository.save(qrCode);

        String json = formatarQrCodeJson(qrCode);
        salvarQrCodeComoImagem(json);
    }

    /**
     * Gera dados do QR Code com data de vencimento e salva no repositório.
     *
     * @param id o ID do QR Code
     * @param valor o valor do QR Code
     * @param descricao a descrição do QR Code
     * @param status o status do QR Code
     * @param dataAtualizacao a data de atualização do QR Code
     * @param dataExpiracao a data de expiração do QR Code
     * @param dataVencimento a data de vencimento do QR Code
     */
    public void gerarDadosQrCodeVencimento(Long id, String valor, String descricao, String status, LocalDate dataAtualizacao, LocalDate dataExpiracao, LocalDate dataVencimento) {
        validarDadosQrCode(id, valor, dataAtualizacao, dataExpiracao);
        validarDataVencimento(dataVencimento);

        QrCode qrCode = persistirDados(id, valor, descricao, status, dataAtualizacao, dataExpiracao, dataVencimento);
        qrCodeRepository.save(qrCode);

        String json = formatarQrCodeJson(qrCode);
        salvarQrCodeComoImagem(json);
    }

    /**
     * Cria um objeto QrCode com os dados fornecidos.
     *
     * @param id o ID do QR Code
     * @param valor o valor do QR Code
     * @param descricao a descrição do QR Code
     * @param status o status do QR Code
     * @param dataAtualizacao a data de atualização do QR Code
     * @param dataExpiracao a data de expiração do QR Code
     * @param dataVencimento a data de vencimento do QR Code
     * @return o objeto QrCode criado
     */
    private QrCode persistirDados(Long id, String valor, String descricao, String status, LocalDate dataAtualizacao, LocalDate dataExpiracao, LocalDate dataVencimento) {
        QrCode qrCode = new QrCode();
        qrCode.setId(id);
        qrCode.setValor(valor);
        qrCode.setDescricao(descricao);
        qrCode.setStatus(StringUtils.isBlank(status) ? "OPEN" : status);
        qrCode.setDataAtualizacao(dataAtualizacao);
        qrCode.setDataExpiracao(dataExpiracao);
        qrCode.setDataVencimento(dataVencimento);
        return qrCode;
    }

    /**
     * Formata os dados do objeto QrCode em uma string JSON.
     *
     * @param qrCode o objeto QrCode
     * @return a string JSON formatada
     */
    private String formatarQrCodeJson(QrCode qrCode) {
        return String.format("ID: %d\nValor: %s\nDescrição: %s\nStatus: %s\nData de Atualização: %s\nData de Expiração: %s\nData de Vencimento: %s",
                qrCode.getId(), qrCode.getValor(), qrCode.getDescricao(), qrCode.getStatus(), qrCode.getDataAtualizacao(), qrCode.getDataExpiracao(), qrCode.getDataVencimento());
    }

    /**
     * Salva os dados do QR Code como uma imagem.
     *
     * @param data os dados do QR Code
     */
    private void salvarQrCodeComoImagem(String data) {
        try {
            String path = prepararLocalDestinoQr();
            processarQrCode(data, path, "UTF-8", 400, 400);
        } catch (WriterException | IOException e) {
            log.error("Erro ao salvar QR Code como imagem", e);
        }
    }

    /**
     * Valida os dados do QR Code.
     *
     * @param id o ID do QR Code
     * @param valor o valor do QR Code
     * @param dataAtualizacao a data de atualização do QR Code
     * @param dataExpiracao a data de expiração do QR Code
     */
    public void validarDadosQrCode(Long id, String valor, LocalDate dataAtualizacao, LocalDate dataExpiracao) {
        if (id == null) {
            throw new IllegalArgumentException("O ID não pode ser nulo");
        }
        if (StringUtils.isBlank(valor) || Double.parseDouble(valor) <= 0) {
            throw new IllegalArgumentException("Por favor, insira um valor válido");
        }
        if (dataAtualizacao == null) {
            throw new IllegalArgumentException("Data de Atualização é obrigatória");
        }
        if (dataExpiracao == null) {
            throw new IllegalArgumentException("Data de Expiração é obrigatória");
        }
    }

    /**
     * Valida a data de vencimento do QR Code.
     *
     * @param dataVencimento a data de vencimento do QR Code
     */
    public void validarDataVencimento(LocalDate dataVencimento) {
        if (dataVencimento == null || dataVencimento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A Data de Vencimento é obrigatória e deve ser maior que a data atual");
        }
    }

    /**
     * Prepara o caminho de destino para salvar a imagem do QR Code.
     *
     * @return o caminho de destino
     */
    private String prepararLocalDestinoQr() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat(DATA_FORMATADA);
        String formattedDate = dateFormat.format(date);

        File directory = new File(DESTINO_ARQUIVO);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return DESTINO_ARQUIVO + "\\" + "QRCode-" + formattedDate + ".png";
    }

    /**
     * Processa os dados do QR Code e salva como uma imagem.
     *
     * @param data os dados do QR Code
     * @param path o caminho de destino
     * @param charset o conjunto de caracteres
     * @param height a altura da imagem
     * @param width a largura da imagem
     * @throws WriterException se ocorrer um erro ao escrever o QR Code
     * @throws IOException se ocorrer um erro ao salvar a imagem
     */
    private void processarQrCode(String data, String path, String charset, int height, int width) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }
}