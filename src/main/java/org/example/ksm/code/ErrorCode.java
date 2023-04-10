package org.example.ksm.code;

/**
 * Enum 클래스 - 에러코드
 *
 * NOTE :
 * @author htkim
 * @since 2019.11.04
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.04   htkim         최초 생성
 *
 */
public enum ErrorCode {

    SUCCESS(0, "성공"),
    UNAUTHORIZED_INFORMATION(100, "미인가정보(차량, 시설물)"),
    UNAUTHORIZED_ACCESS(200, "미인가접속"),
    ABNORMAL_MESSAGE_HEADER(301, "비정상메시지 - Header 비정상"),
    ABNORMAL_MESSAGE_LENGTH(302, "비정상메시지 - Length 비정상")
    ;

    private int code;
    private String name;
    private ErrorCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
