package dev.rennen;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

public class ResponseServletOutputStream extends ServletOutputStream {

    private byte[] bytes = new byte[1024];
    private int pos = 0;

    @Override
    public void write(int b) throws IOException {
        bytes[pos++] = (byte) b;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getPos() {
        return pos;
    }
}
