package org.example.ksm.code;

/**
 * Enum 클래스 - 제어명령 - 원격 제어 코드
 *
 * NOTE :
 * @author naeunhye
 * @since 2022.11.04
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2022.11.04   naeunhye       최초 생성
 *
 */
public enum AutoControlCode {

    CONTROL_REQUEST("001", "Req"),
    CONTROL_ACCEPT("001", "Acc"),
    CONTROL_REJECT("001", "Rej"),
    CONTROL_TERMINATION("001", "Ter"),

    COCKPIT("002", "01"),
    MEDIATOR("002", "02")
    ;
    private String code;
    private String name;
    private AutoControlCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
