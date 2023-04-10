package org.example.ksm.bus;

/**
 * Enum 클래스 - EventBus 주소
 *
 * NOTE :
 * @author htkim
 * @since 2019.10.29
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.10.29   htkim         최초 생성
 *
 */
public enum EventBusAddress {
    KSM_TARGET_VERTICLE("ksm.publish.target.verticle"), // KSM - 지정 차량으로 데이터 보내는 주소
    KSM_ALL("ksm.publish.netsocket.verticle"),          // KSM - 모든 차량으로 데이터 보내는 주소
    ;

    private String address;

    EventBusAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
