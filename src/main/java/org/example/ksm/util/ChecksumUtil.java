package org.example.ksm.util;


import io.vertx.core.buffer.Buffer;
import org.example.ksm.coder.KSMEncoder;
import org.example.ksm.dto.KSMFrame;
import org.example.ksm.dto.KSMFrameHeader;

import java.util.zip.CRC32;

/**
 * Checksum - CRC32
 *
 * NOTE :
 * @author htkim
 * @since 2019.11.05
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.05   htkim         최초 생성
 *
 */
public abstract class ChecksumUtil {

    private static KSMEncoder encoder = new KSMEncoder();

    /**
     * set checksum
     *
     * @param frame ksmframe
     * @return ksmframe
     */
    public static KSMFrame appendCheckSum(KSMFrame frame) {
        frame.getHeader().setCheckSum(0L);
        long csResult = checksum(frame.encode(encoder).getBytes());
        frame.getHeader().setCheckSum(csResult);
        return frame;
    }

    /**
     * encode buffer after setting checksum
     *
     * @param frame - ksmframe
     * @return buffer
     */
    public static Buffer appendCheckSumToBufferResult(KSMFrame frame) {
        KSMFrame appendCheckSumFrame = appendCheckSum(frame);
        return appendCheckSumFrame.encode(encoder);
    }

    /**
     * get checksum
     *
     * @param datas - ksmframe data
     * @return checksum
     */
    private static long checksum(byte [] datas) {
        CRC32 crc32 = new CRC32();
        crc32.update(datas);
        return crc32.getValue();
    }

    /**
     * encode buffer after setting checksum
     *
     * @param header - ksmframe Header
     * @return buffer
     */
    public static Buffer appendCheckSumToBufferResult(KSMFrameHeader header) {
        header.setCheckSum(0L);
        long csResult = checksum(header.encode(encoder).getBytes());
        header.setCheckSum(csResult);
        return header.encode(encoder);
    }
}
