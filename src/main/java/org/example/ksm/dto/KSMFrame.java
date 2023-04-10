package org.example.ksm.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.vertx.core.buffer.Buffer;
import org.example.ksm.coder.IKSMFrameDecodeVisitor;
import org.example.ksm.coder.IKSMFrameEncodeVisitor;

/**
 * KSMFrame 클래스
 *
 * NOTE :
 * @author htkim
 * @since 2019.10.28
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.10.28   htkim         최초 생성
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public  class KSMFrame extends AbstractDataFrame {
    private KSMFrameHeader header;
    private KSMFrameBody body;
    private String writeHandlerID;
    private Long msgRcvTime;
    private String edgeId;

    public KSMFrameHeader getHeader() {
        return header;
    }

    public void setHeader(KSMFrameHeader header) {
        this.header = header;
    }

    public KSMFrameBody getBody() {
        return body;
    }

    public void setBody(KSMFrameBody body) {
        this.body = body;
    }

    public String getWriteHandlerID() {
        return writeHandlerID;
    }

    public void setWriteHandlerID(String writeHandlerID) {
        this.writeHandlerID = writeHandlerID;
    }

    public Long getMsgRcvTime() {
        return msgRcvTime;
    }

    public void setMsgRcvTime(Long msgRcvTime) {
        this.msgRcvTime = msgRcvTime;
    }

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

    @Override
    public String toString() {
        return "KSMFrame{" +
                "header=" + header +
                ", body=" + body +
                ", writeHandlerID='" + writeHandlerID + '\'' +
                ", msgRcvTime=" + msgRcvTime +
                ", edgeId='" + edgeId + '\'' +
                '}';
    }

    @Override
    public Buffer encode(IKSMFrameEncodeVisitor visitor) {
        return visitor.encode(this);
    }

    @Override
    public AbstractDataFrame decode(Buffer buffer, IKSMFrameDecodeVisitor visitor) {
        return visitor.decode(buffer, this);
    }
}
