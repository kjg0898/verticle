package org.example.ksm.coder;


import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.example.ksm.dto.KSMFrame;
import org.example.ksm.dto.KSMFrameBody;
import org.example.ksm.dto.KSMFrameHeader;

/**
 * Decoder
 *
 * NOTE :
 * @author sojeong
 * @since 2019.11.07
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.07   sojeong         최초 생성
 *
 */
public class KSMDecoder implements IKSMFrameDecodeVisitor {

    /**
     * Decode KSMFrame Buffer
     * @param buffer - ksmframe buffer
     * @param frame - ksmframe
     * @return ksmframe
     */
    @Override
    public KSMFrame decode(Buffer buffer, KSMFrame frame) {
        int headerLength = KSMFrameHeader.LengthInfo.BodyLength.getEnd();
        frame.setHeader(this.decode(buffer.getBuffer(0, headerLength), new KSMFrameHeader()));
        if( headerLength < buffer.length() ) {
            frame.setBody(this.decode(buffer.getBuffer(headerLength, buffer.length()), new KSMFrameBody()));
        }
        return frame;
    }

    /**
     * Decode KSMFrame Header Buffer
     * @param buffer - ksmframe header buffer
     * @param header - ksmframe header
     * @return ksmframe Header
     */
    @Override
    public KSMFrameHeader decode(Buffer buffer, KSMFrameHeader header) {
        KSMFrameHeader.LengthInfo lengthInfo = KSMFrameHeader.LengthInfo.CarKey;
        header.setCarKey(buffer.getString(lengthInfo.getStart(), lengthInfo.getEnd()).trim());
        lengthInfo = KSMFrameHeader.LengthInfo.AccessKey;
        header.setAccessKey(buffer.getString(lengthInfo.getStart(), lengthInfo.getEnd()).trim());
        lengthInfo = KSMFrameHeader.LengthInfo.CreateTime;
        header.setCreateTime(buffer.getString(lengthInfo.getStart(), lengthInfo.getEnd()).trim());
        lengthInfo = KSMFrameHeader.LengthInfo.CmdCode;
        header.setCmdCode(buffer.getUnsignedShort(lengthInfo.getStart()));
        lengthInfo = KSMFrameHeader.LengthInfo.InfoCode;
        header.setInfoCode(buffer.getUnsignedShort(lengthInfo.getStart()));
        lengthInfo = KSMFrameHeader.LengthInfo.ErrorCode;
        header.setErrorCode(buffer.getUnsignedShort(lengthInfo.getStart()));
        lengthInfo = KSMFrameHeader.LengthInfo.CheckSum;
        header.setCheckSum(buffer.getUnsignedInt(lengthInfo.getStart()));
        lengthInfo = KSMFrameHeader.LengthInfo.BodyLength;
        header.setBodyLength(buffer.getUnsignedInt(lengthInfo.getStart()));
        return header;
    }

    /**
     * decode KSMFrame body buffer
     * @param buffer - ksmframe body buffer
     * @param body ksmframe body
     * @return ksmframe Body
     */
    @Override
    public KSMFrameBody decode(Buffer buffer, KSMFrameBody body) {
        body.setBody(new JsonObject(buffer));
        return body;
    }
}
