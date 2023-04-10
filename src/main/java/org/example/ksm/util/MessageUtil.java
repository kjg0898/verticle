package org.example.ksm.util;/*
package org.example.ksm.util;



import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

*/
/**
 * MessageUtil 클래스
 *
 * NOTE :
 * @author htkim
 * @since 2019.11.08
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.08   htkim         최초 생성
 *
 *//*

public abstract class MessageUtil {

    */
/**
     * make custom car id
     *
     * @param values - element
     * @return custom car id
     *//*

    public static String customCarId(String ... values) {
        return Arrays.stream(values).collect(Collectors.joining(":"));
    }

    public static UnnormalLog createUnnormal(String uuid, String serviceName, String serviceType, String stateInfo, Object info) {

        long logTime = System.currentTimeMillis();
        UnnormalLog uLog = new UnnormalLog();
        uLog.setUuid(uuid);
        State state = new State();
        uLog.setMessageState(state);
        uLog.setLogTime(logTime);
        uLog.setServiceName(serviceName);
        uLog.setInfo(info.toString());

        state.setTime(logTime);
        state.setType(serviceType);
        state.setInfo(stateInfo);
        return uLog;
    }

    public static UnnormalLog createUnnormal(String serviceName, String serviceType, String stateInfo, Object info) {
        return createUnnormal(UUID.randomUUID().toString(), serviceName, serviceType, stateInfo, info);
    }

    public static UnnormalLog createSuccessMessage(String serviceName, Object info) {
        return createUnnormal(serviceName, "성공", "메시지수집", info);
    }

    public static UnnormalLog createFailMessage(String serviceName, Object info) {
        return createUnnormal(serviceName, "실패", "메시지수집", info);
    }

    public static UnnormalLog createLogStartMessage(String uuid, String serviceName, Object info) {
        return createUnnormal(uuid, serviceName, "시작", "메시지수집", info);
    }

    public static UnnormalLog createLogEndMessage(String uuid, String serviceName, Object info) {
        return createUnnormal(uuid, serviceName, "종료", "메시지수집", info);
    }
}
*/
