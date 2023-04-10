package org.example.ksm.code;

/**
 * Enum 클래스 - 차량정보, 시설물정보 상세코드
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
public enum SubCode {
    NONE(0, "없음"),
    CAR_MASTER_ICE(1, "차량정보마스터-내연기관"),
    CAR_MASTER_PEV(2, "차량정보마스터-전기차"),
    CAR_MASTER_ROBOT(3, "차량정보마스터-Robot"),
    CAR_MASTER_CHARGE(4, "차량정보마스터-충전베터리"),
    CAR_MASTER_EVENT(5, "차량정보마스터-이벤트"),
    CAR_MASTER_DAM(6, "차량정보마스터-DAM"),
    CAR_MASTER_SAIM(7, "차량정보마스터-SAIM"),
    FACILITY_TRAFFIC(0, "시설물정보마스터-신호등"),
    FACILITY_SENSOR(1, "시설물정보마스터-노변센서"),
    FACILITY_DETECTOR(2, "시설물정보마스터-돌발(보행자)검지기"),
    FACILITY_AIVISON(3, "시설물정보마스터-AI비전감지기"),

    CONTROL_MOVE(200, "이동 제어코드"),
    CONTROL_REMOTE(300, "원격 제어코드"),
    CONTROL_COLLECT(400, "제어명령 이력")
    ;
    private int code;
    private String name;
    private SubCode(int code, String name) {
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
