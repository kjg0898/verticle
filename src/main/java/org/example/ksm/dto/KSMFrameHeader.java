package org.example.ksm.dto;


import io.vertx.core.buffer.Buffer;
import org.example.ksm.coder.IKSMFrameDecodeVisitor;
import org.example.ksm.coder.IKSMFrameEncodeVisitor;

/**
 * KSMFrame Header 클래스
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
public class KSMFrameHeader extends AbstractDataFrame {
    private String carKey;
    private String accessKey;
    private String createTime;
    private Integer cmdCode;
    private Integer infoCode;
    private Integer errorCode;
    private Long checkSum;
    private Long bodyLength;

    public String getCarKey() {
        return carKey;
    }

    public void setCarKey(String carKey) {
        this.carKey = carKey;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(Integer cmdCode) {
        this.cmdCode = cmdCode;
    }

    public Integer getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(Integer infoCode) {
        this.infoCode = infoCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Long getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Long checkSum) {
        this.checkSum = checkSum;
    }

    public Long getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(Long bodyLength) {
        this.bodyLength = bodyLength;
    }

    public enum LengthInfo {
        CarKey(0,50, "String"),
        AccessKey(CarKey.end, CarKey.end + 300, "String"),
        CreateTime(AccessKey.end, AccessKey.end + 15, "String"),
        CmdCode(CreateTime.end, CreateTime.end + 2, "UnsignedInt"),
        InfoCode(CmdCode.end, CmdCode.end + 2, "UnsignedInt"),
        ErrorCode(InfoCode.end, InfoCode.end + 2, "UnsignedInt"),
        CheckSum(ErrorCode.end, ErrorCode.end + 4, "UnsignedInt"),
        BodyLength(CheckSum.end, CheckSum.end + 4, "UnsignedInt"),
        ;

        private int start;
        private int end;
        private String readTypeName;

        private LengthInfo(int start, int end, String readType) {
            this.start = start;
            this.end = end;
            readTypeName = readType;
        }
        public int getStart() {
            return start;
        }
        public int getEnd() {
            return end;
        }

        public String getReadTypeName() {
            return readTypeName;
        }

        public int getSize() {
            return end - start;
        }
    }

    @Override
    public Buffer encode(IKSMFrameEncodeVisitor visitor) {
        return visitor.encode(this);
    }

    @Override
    public AbstractDataFrame decode(Buffer buffer, IKSMFrameDecodeVisitor visitor) {
        return visitor.decode(buffer, this);
    }

    @Override
    public String toString() {
        return "KSMFrameHeader{" +
                "carKey='" + carKey + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", createTime='" + createTime + '\'' +
                ", cmdCode=" + cmdCode +
                ", infoCode=" + infoCode +
                ", errorCode=" + errorCode +
                ", checkSum='" + checkSum + '\'' +
                ", bodyLength=" + bodyLength +
                '}';
    }
}
