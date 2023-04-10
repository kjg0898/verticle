package org.example.ksm.code;

/**
 * Enum 클래스 - 정보코드
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
public enum  InfoCode {
    COLLECT_CAR(0, "차량정보"),
    COLLECT_FACILITY(100, "시설물정보"),
    OFFER_COMMAND(801, "제어명령"),
    OFFER_BASIS(802, "설정명령"),
    OFFER_TIM(901, "K-TIM"),
    OFFER_RSA(902, "K-RSA"),
    OFFER_SPAT(903, "K-SPAT"),
    OFFER_EVA(904, "K-EVA"),
    OFFER_MAP(906, "K-MAP"),
    OFFER_RTCM(907, "K-RTCM"),
    OFFER_SAOM(908, "SAOM"),
    OFFER_BSM(909, "K-BSM"),
    OFFER_SMAP(910, "K-SMAP"),
    OFFER_DMAP(911, "K-DMAP")
    ;

    private int code;
    private String name;
    private InfoCode(int code, String name) {
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
