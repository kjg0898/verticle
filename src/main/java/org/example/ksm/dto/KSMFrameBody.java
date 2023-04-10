package org.example.ksm.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.example.ksm.coder.IKSMFrameDecodeVisitor;
import org.example.ksm.coder.IKSMFrameEncodeVisitor;
import org.example.ksm.coder.JsonObjectDeserializer;


/**
 * KSMFrame Body 클래스
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
public class KSMFrameBody extends AbstractDataFrame {

    @JsonDeserialize(using= JsonObjectDeserializer.class)
    private JsonObject body;

    public JsonObject getBody() {
        return body;
    }

    public void setBody(JsonObject body) {
        this.body = body;
    }

    @Override
    public AbstractDataFrame decode(Buffer buffer, IKSMFrameDecodeVisitor visitor) {
        return visitor.decode(buffer, this);
    }

    @Override
    public Buffer encode(IKSMFrameEncodeVisitor visitor) {
        return visitor.encode(this);
    }

    @Override
    public String toString() {
        return "KSMFrameBody{" +
                "body=" + body +
                '}';
    }
}
