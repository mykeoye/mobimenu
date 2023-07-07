package io.mobimenu.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public final class QrCodeProvider {

    public static BufferedImage generate(String text, int width, int height) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private static String imageToBase64(BufferedImage bufferedImage) {
        var image = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", outputStream);
            image = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            outputStream.close();
        } catch (IOException exception) {
           log.error("Exception converting image to base64 string ", exception);
        }
        return image;
    }

    public static String generateImage(String text, int width, int height) {
        try {
            return imageToBase64(generate(text, width, height));
        } catch (WriterException exception) {
            log.error("Exception generating qrCode ", exception);
        }
        return null;
    }

}
