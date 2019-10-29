package cphb.ufo;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws Exception {
        App app = new App();
        BufferedImage image = app.getImage("https://datsoftlyngby.github.io/soft2019fall/UFO/images/Stego.png");
        byte[] bits = app.getMessageBits(image);
        String msg = app.assembleBits(bits);
        System.out.println("The message is: " + msg);
    }

    private BufferedImage getImage(String url) throws IOException {
        return ImageIO.read(new URL(url));
    }

    private byte[] getMessageBits(BufferedImage img) {
        ByteBuffer buffer = ByteBuffer.allocate(img.getWidth() * img.getHeight());
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                int pixel = img.getRGB(j, i);
                byte msgBit = (byte)(pixel & 0b1);
                buffer.put(msgBit);
            }
        }
        return buffer.array();
    }

    private String assembleBits(byte[] bytes) {
        StringBuilder s = new StringBuilder();
        int count = 0;
        byte b = 0;
        for (byte bit : bytes) {
            // Every 8 iterations, convert byte to char
            if (count > 0 && count % 8 == 0) {
                if (b == 0) break; // '\0' is the terminator
                s.append((char)b);
                b = 0;
            }

            byte pos = (byte)(count % 8);
            // Set the bits value
            b = (byte)(b | (bit << pos));
            count++;

        }
        return s.toString();
    }
}
