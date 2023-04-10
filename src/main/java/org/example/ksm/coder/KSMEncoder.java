package org.example.ksm.coder;

import io.vertx.core.buffer.Buffer;
import org.example.ksm.dto.KSMFrame;
import org.example.ksm.dto.KSMFrameBody;
import org.example.ksm.dto.KSMFrameHeader;

import java.util.Arrays;

/**
 * KSM Encoder
 *
 * NOTE :
 * @author htkim
 * @since 2019.11.06
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.06   htkim         최초 생성
 *
 */
public class KSMEncoder implements IKSMFrameEncodeVisitor {

    /**
     * Encode KSMFrame Header
     *
     * @param header - ksmframe header
     * @return buffer
     */
    @Override
    public Buffer encode(KSMFrameHeader header) {
        Buffer buffer = Buffer.buffer();
//        buffer.appendString(appendSpace(header.getCarKey(), KSMFrameHeader.LengthInfo.CarKey.getSize()));
//        buffer.appendString(appendSpace(header.getAccessKey(), KSMFrameHeader.LengthInfo.AccessKey.getSize()));
        buffer.appendBytes(appendNullByte(header.getCarKey(), KSMFrameHeader.LengthInfo.CarKey.getSize()));
        buffer.appendBytes(appendNullByte(header.getAccessKey(), KSMFrameHeader.LengthInfo.AccessKey.getSize()));
        buffer.appendBytes(appendNullByte(header.getCreateTime(), KSMFrameHeader.LengthInfo.CreateTime.getSize()));
        buffer.appendUnsignedShort(header.getCmdCode());
        buffer.appendUnsignedShort(header.getInfoCode());
        buffer.appendUnsignedShort(header.getErrorCode());
        buffer.appendUnsignedInt(header.getCheckSum());
        buffer.appendUnsignedInt(header.getBodyLength());
        return buffer;
    }

    /**
     * Encode KSMFrame
     *
     * @param frame - ksmframe
     * @return buffer
     */
    @Override
    public Buffer encode(KSMFrame frame) {
        Buffer buffer = Buffer.buffer();
        if( frame.getHeader() != null ) {
            buffer.appendBuffer(this.encode(frame.getHeader()));
        }
        if( frame.getBody() != null ) {
            buffer.appendBuffer(this.encode(frame.getBody()));
        }
        return buffer;
    }

    /**
     * Encode KSMFrame body
     *
     * @param body - ksmframe body
     * @return buffer
     */
    @Override
    public Buffer encode(KSMFrameBody body) {
        return Buffer.buffer(body.getBody().toString());
    }

    /**
     * append space
     * @param s - header field
     * @param len - header field size
     * @return header field
     */
    private String appendSpace(String s, int len) {
        if (s.length() > len) {
            return s.substring(0, len);
        }

        int spaceLen = len - s.length();
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        for (int i = 0; i < spaceLen; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    /**
     * append null byte
     * @param s - header field
     * @param len - header field size
     * @return header field append null byte
     */
    private byte [] appendNullByte(String s, int len) {
        byte [] bStr = s.getBytes();

        if( bStr.length > len ) {
            return Arrays.copyOf(bStr, len);
        }

        byte [] dest = new byte [len];
        System.arraycopy(bStr, 0, dest, 0, bStr.length);
        return dest;
    }
}
