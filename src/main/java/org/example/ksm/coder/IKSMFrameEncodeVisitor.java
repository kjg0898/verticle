package org.example.ksm.coder;


import io.vertx.core.buffer.Buffer;
import org.example.ksm.dto.KSMFrame;
import org.example.ksm.dto.KSMFrameBody;
import org.example.ksm.dto.KSMFrameHeader;

/**
 * Encode Visitor
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
public interface IKSMFrameEncodeVisitor {
    public Buffer encode(KSMFrameHeader header);
    public Buffer encode(KSMFrame frame);
    public Buffer encode(KSMFrameBody body);
}
