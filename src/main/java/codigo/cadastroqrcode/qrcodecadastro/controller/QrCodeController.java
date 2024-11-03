package codigo.cadastroqrcode.qrcodecadastro.controller;

import codigo.cadastroqrcode.qrcodecadastro.dto.QrCodeDTO;
import codigo.cadastroqrcode.qrcodecadastro.service.QrCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QrCodeController {

    @Autowired
    private QrCodeService qrCodeService;

    @Operation(summary = "Cadastra QrCode Imediato", description = "Cadastra e gera um QR Code imediato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QR Code gerado com sucesso"),
            @ApiResponse(responseCode = "404", description = "QR Code não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/cadastroQrCode")
    public String cadastroQrCodeImediato(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do QR Code a ser cadastrado")
            @RequestBody QrCodeDTO qrCodeDTO) {
        qrCodeService.gerarDadosQrCode(qrCodeDTO.getId(), qrCodeDTO.getValor(), qrCodeDTO.getDescricao(), qrCodeDTO.getStatus(),
                qrCodeDTO.getDataAtualizacao(), qrCodeDTO.getDataExpiracao());
        return "QR Code Imediato Cadastrado com Sucesso!";
    }

    @Operation(summary = "Cadastra QrCode Imediato com Data de Vencimento", description = "Cadastra e gera um QR Code imediato com data de vencimento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QR Code gerado com sucesso"),
            @ApiResponse(responseCode = "404", description = "QR Code não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/cadastroQrCodeVencimento")
    public String cadastroQrCodeImediatoDataVencimento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do QR Code a ser cadastrado")
            @RequestBody QrCodeDTO qrCodeDTO) {
        qrCodeService.gerarDadosQrCodeVencimento(qrCodeDTO.getId(), qrCodeDTO.getValor(), qrCodeDTO.getDescricao(), qrCodeDTO.getStatus(),
                qrCodeDTO.getDataAtualizacao(), qrCodeDTO.getDataExpiracao(), qrCodeDTO.getDataVencimento());
        return "QR Code Imedato com Vencimento Cadastrado com Sucesso!";
    }
}