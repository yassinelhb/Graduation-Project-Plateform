package tn.esprit.pfe.qrcode;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.FileSystems;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import tn.esprit.pfe.entities.SheetPFE;

public class QRCode {

	public void writeQRCode(SheetPFE sheetPFE) throws WriterException, IOException {
		
		String qcodePath = "C:\\Users\\lhbya\\git\\4twin3-osp-pfe\\4twin3-osp-pfe-ejb\\src\\main\\resources\\QRCode\\" + sheetPFE.getQrcode() + ".png";
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(sheetPFE.getTitle() + "\n" + sheetPFE.getDescription() + "\n"  + sheetPFE.getProblematic() , BarcodeFormat.QR_CODE, 350, 350);
		Path path = FileSystems.getDefault().getPath(qcodePath);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
		
	}
}
