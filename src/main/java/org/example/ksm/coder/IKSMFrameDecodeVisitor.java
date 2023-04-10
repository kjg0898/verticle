package org.example.ksm.coder;


import io.vertx.core.buffer.Buffer;
import org.example.ksm.dto.KSMFrame;
import org.example.ksm.dto.KSMFrameBody;
import org.example.ksm.dto.KSMFrameHeader;

/**
 * Decode Visitor
 *
 * NOTE :
 * @author sojeong
 * @since 2019.11.06
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.06   sojeong         최초 생성
 *
 */
public interface IKSMFrameDecodeVisitor {
    public KSMFrame decode(Buffer buffer, KSMFrame frame);
    public KSMFrameHeader decode(Buffer buffer, KSMFrameHeader frame);
    public KSMFrameBody decode(Buffer buffer, KSMFrameBody frame);
}
