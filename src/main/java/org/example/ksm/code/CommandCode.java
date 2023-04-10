package org.example.ksm.code;

/**
 * Enum 클래스 - 명령코드
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
public enum CommandCode {
    REQUEST_CAR(100, "접속요청(차량)"),
    REQUEST_FACILITY(110, "접속요청(시설물)"),
    RESPONSE_CAR(101, "접속응답(차량)"),
    RESPONSE_FACILITY(111, "접속응답(시설물)"),
    SEND_FROM_CAR(200, "차량->플랫폼 정보전송"),
    RESPONSE_FROM_CAR(201, "차량->플랫폼 정보전송 응답"),
    SEND_FROM_FACILITY(210, "시설물->플랫폼 정보전송"),
    RESPONSE_FROM_FACILITY(211, "시설물->플랫폼 정보전송 응답"),
    SEND_FROM_PLATFORM(300, "플랫폼->차량 정보전송"),
    RESPONSE_FROM_PLATFORM(301, "플랫폼->차량 정보전송 응답")
    ;

    private int code;
    private String name;
    private CommandCode(int code, String name) {
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
