package org.example.ksm.key;

/**
 * Redis Common Key 클래스
 *
 * NOTE :
 * @author htkim
 * @since 2019.11.01
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.01   htkim         최초 생성
 *
 */
public enum REDIS_CAR_KEY {

    AGV_SESSION_INFO("AGV_SESSION_INFO"),
    CAR_SESSION_INFO("CAR_SESSION_INFO"),
    EXPIRE_DIST_LINK("EXPIRE_DIST_LINK"),
    ACTIVE_VEHICLE_RADIUS("ACTIVE_VEHICLE_RADIUS"),
    EXPIRE_ACTIVE_VEHICLE_RADIUS("EXPIRE_ACTIVE_VEHICLE_RADIUS"),
    LDM_TIME("LDM_TIME"),
    ;

    private String key;

    REDIS_CAR_KEY(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
