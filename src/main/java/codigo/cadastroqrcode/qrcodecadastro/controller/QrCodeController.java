package codigo.cadastroqrcode.qrcodecadastro.controller;

import codigo.cadastroqrcode.qrcodecadastro.dto.QrCodeDTO;
import codigo.cadastroqrcode.qrcodecadastro.service.QrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;

    @PostMapping("/cadastroQrCode")
    public String cadastroQrCodeImediato(@RequestBody QrCodeDTO qrCodeDTO) {
        qrCodeService.gerarDadosQrCode(qrCodeDTO.getId(), qrCodeDTO.getValor(), qrCodeDTO.getDescricao(), qrCodeDTO.getStatus(),
                qrCodeDTO.getDataAtualizacao(), qrCodeDTO.getDataExpiracao());
        return "QR Code Imediato Cadastrado com Sucesso!";
    }

    @PostMapping("/cadastroQrCodeVencimento")
    public String cadastroQrCodeImediatoDataVencimento(@RequestBody QrCodeDTO qrCodeDTO) {
        qrCodeService.gerarDadosQrCodeVencimento(qrCodeDTO.getId(), qrCodeDTO.getValor(), qrCodeDTO.getDescricao(), qrCodeDTO.getStatus(),
                qrCodeDTO.getDataAtualizacao(), qrCodeDTO.getDataExpiracao(), qrCodeDTO.getDataVencimento());
        return "QR Code Imedato com Vencimento Cadastrado com Sucesso!";
    }
}