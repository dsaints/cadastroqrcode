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
     * Gera os dados do QR Code e salva no repositório.
     *
     * @param id O ID do QR Code.
     * @param valor O valor do QR Code.
     * @param descricao A descrição do QR Code.
     * @param status O status do QR Code.
     * @param dataAtualizacao A data de atualização do QR Code.
     * @param dataExpiracao A data de expiração do QR Code.
     */
    public void gerarDadosQrCode(Long id, String valor, String descricao, String status, LocalDate dataAtualizacao, LocalDate dataExpiracao) {
        validarDadosQrCode(id, valor, dataAtualizacao, dataExpiracao);
        QrCode qrCode = criarQrCode(id, valor, descricao, status, dataAtualizacao, dataExpiracao, null);
        salvarQrCode(qrCode);
    }

    /**
     * Gera os dados do QR Code com data de vencimento e salva no repositório.
     *
     * @param id O ID do QR Code.
     * @param valor O valor do QR Code.
     * @param descricao A descrição do QR Code.
     * @param status O status do QR Code.
     * @param dataAtualizacao A data de atualização do QR Code.
     * @param dataExpiracao A data de expiração do QR Code.
     * @param dataVencimento A data de vencimento do QR Code.
     */
    public void gerarDadosQrCodeVencimento(Long id, String valor, String descricao, String status, LocalDate dataAtualizacao, LocalDate dataExpiracao, LocalDate dataVencimento) {
        validarDadosQrCode(id, valor, dataAtualizacao, dataExpiracao);
        validarDataVencimento(dataVencimento);
        QrCode qrCode = criarQrCode(id, valor, descricao, status, dataAtualizacao, dataExpiracao, dataVencimento);
        salvarQrCode(qrCode);
    }

    /**
     * Cria um objeto QrCode com os dados fornecidos.
     *
     * @param id O ID do QR Code.
     * @param valor O valor do QR Code.
     * @param descricao A descrição do QR Code.
     * @param status O status do QR Code.
     * @param dataAtualizacao A data de atualização do QR Code.
     * @param dataExpiracao A data de expiração do QR Code.
     * @param dataVencimento A data de vencimento do QR Code.
     * @return O objeto QrCode criado.
     */
    private QrCode criarQrCode(Long id, String valor, String descricao, String status, LocalDate dataAtualizacao, LocalDate dataExpiracao, LocalDate dataVencimento) {
        QrCode qrCode = new QrCode();
        qrCode.setId(id);
        qrCode.setValor(valor);
        qrCode.setDescricao(descricao);
        qrCode.setStatus(obterStatus(status));
        qrCode.setDataAtualizacao(dataAtualizacao);
        qrCode.setDataExpiracao(dataExpiracao);
        qrCode.setDataVencimento(dataVencimento);
        return qrCode;
    }

    /**
     * Obtém o status do QR Code, retornando "OPEN" se o status estiver em branco.
     *
     * @param status O status do QR Code.
     * @return O status do QR Code.
     */
    private String obterStatus(String status) {
        return StringUtils.isBlank(status) ? "OPEN" : status;
    }

    /**
     * Salva o QR Code no repositório e gera a imagem do QR Code.
     *
     * @param qrCode O objeto QrCode a ser salvo.
     */
    private void salvarQrCode(QrCode qrCode) {
        qrCodeRepository.save(qrCode);
        String json = formatarQrCodeJson(qrCode);
        salvarQrCodeComoImagem(json);
    }

    /**
     * Formata os dados do QR Code em formato JSON.
     *
     * @param qrCode O objeto QrCode a ser formatado.
     * @return Os dados do QR Code em formato JSON.
     */
    private String formatarQrCodeJson(QrCode qrCode) {
        return String.format("ID: %d\nValor: %s\nDescrição: %s\nStatus: %s\nData de Atualização: %s\nData de Expiração: %s\nData de Vencimento: %s",
                qrCode.getId(), qrCode.getValor(), qrCode.getDescricao(), qrCode.getStatus(), qrCode.getDataAtualizacao(), qrCode.getDataExpiracao(), qrCode.getDataVencimento());
    }

    /**
     * Salva a imagem do QR Code no diretório especificado.
     *
     * @param data Os dados do QR Code a serem salvos como imagem.
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
     * Prepara o diretório de destino para salvar a imagem do QR Code.
     *
     * @return O caminho do diretório de destino.
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
     * Processa os dados do QR Code e gera a imagem.
     *
     * @param data Os dados do QR Code.
     * @param path O caminho do arquivo de destino.
     * @param charset O charset a ser utilizado.
     * @param height A altura da imagem.
     * @param width A largura da imagem.
     * @throws WriterException Se ocorrer um erro ao gerar a imagem.
     * @throws IOException Se ocorrer um erro de I/O.
     */
    private void processarQrCode(String data, String path, String charset, int height, int width) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }

    /**
     * Valida os dados do QR Code.
     *
     * @param id O ID do QR Code.
     * @param valor O valor do QR Code.
     * @param dataAtualizacao A data de atualização do QR Code.
     * @param dataExpiracao A data de expiração do QR Code.
     */
    public void validarDadosQrCode(Long id, String valor, LocalDate dataAtualizacao, LocalDate dataExpiracao) {
        validarId(id);
        validarValor(valor);
        validarData(dataAtualizacao, "Data de Atualização é obrigatória");
        validarData(dataExpiracao, "Data de Expiração é obrigatória");
    }

    /**
     * Valida o ID do QR Code.
     *
     * @param id O ID do QR Code.
     */
    private void validarId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O ID não pode ser nulo");
        }
    }

    /**
     * Valida o valor do QR Code.
     *
     * @param valor O valor do QR Code.
     */
    private void validarValor(String valor) {
        if (StringUtils.isBlank(valor) || Double.parseDouble(valor) <= 0) {
            throw new IllegalArgumentException("Por favor, insira um valor válido");
        }
    }

    /**
     * Valida a data fornecida.
     *
     * @param data A data a ser validada.
     * @param mensagemErro A mensagem de erro a ser exibida se a data for inválida.
     */
    private void validarData(LocalDate data, String mensagemErro) {
        if (data == null) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }

    /**
     * Valida a data de vencimento do QR Code.
     *
     * @param dataVencimento A data de vencimento do QR Code.
     */
    public void validarDataVencimento(LocalDate dataVencimento) {
        if (dataVencimento == null || dataVencimento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A Data de Vencimento é obrigatória e deve ser maior que a data atual");
        }
    }
}