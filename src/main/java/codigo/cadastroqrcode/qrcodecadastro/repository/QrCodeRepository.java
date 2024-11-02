package codigo.cadastroqrcode.qrcodecadastro.repository;

import codigo.cadastroqrcode.qrcodecadastro.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
}
