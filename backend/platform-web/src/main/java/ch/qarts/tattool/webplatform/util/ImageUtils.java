package ch.qarts.tattool.webplatform.util;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.Base64;

public class ImageUtils {

    public static final String JPEG_B64_HEADER = "data:image/jpeg;base64,";

    @SneakyThrows
    public static String fromFile(String resourceName) {
        return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(Path.of(new URI(resourceName)).toFile()));
    }


    @SneakyThrows
    public static BufferedImage parseB64Image(String b64Image) {
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(b64Image);
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }


    @SneakyThrows
    public static String toBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(os)) {
            ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("JPEG").next();
            ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
            jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            // TODO should be configurable
            jpgWriteParam.setCompressionQuality(0.4f);
            jpgWriter.setOutput(outputStream);
            jpgWriter.write(null, new IIOImage(bufferedImage, null, null), jpgWriteParam);
            jpgWriter.dispose();
        }
        return Base64.getEncoder().encodeToString(os.toByteArray());
    }

    public static BufferedImage removeAlphaChannel(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[] pixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
        BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        copy.setRGB(0, 0, width, height, pixels, 0, width);
        return copy;
    }


}
