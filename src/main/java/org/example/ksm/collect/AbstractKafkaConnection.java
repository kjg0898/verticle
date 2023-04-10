package org.example.ksm.collect;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Kafka 연결 클래스
 *
 * NOTE :
 * @author htkim
 * @since 2019.04.29
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.04.29   htkim          최초 생성
 *
 */
public abstract class AbstractKafkaConnection {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKafkaConnection.class);

    protected Vertx vertx;
    protected JsonObject config;

    private Map<String, String> kafkaConfig;

    /**
     * 생성자
     *
     * @param vertx - vertx
     * @param config - config 정보
     */
    public AbstractKafkaConnection(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
        kafkaConfig = convertConfigToMap(this.config);
    }

    /**
     * map형태로 변경
     *
     * @param config - config 정보
     * @return kafka, zookeeper 정보
     */
    protected Map<String, String> convertConfigToMap(JsonObject config) {
        Map<String, String> map = new HashMap<>();

        try {
            JsonObject kconfig = config.getJsonObject("kafka");
            map.put("bootstrap.servers", convertKafkaUrlFormat(kconfig.getString("host"), kconfig.getInteger("port")));
            map.putAll(new ObjectMapper().readValue(kconfig.toString(), Map.class));

            JsonObject zooConfig = kconfig.getJsonObject("zookeeper");

            map.put("zookeeper.server", convertKafkaUrlFormat(zooConfig.getString("host"), zooConfig.getInteger("port")));
            map.putAll(new ObjectMapper().readValue(zooConfig.toString(), Map.class));
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * url 형태로 변경
     *
     * @param ip - ip
     * @param port - port
     * @return ip:port
     */
    protected  String convertKafkaUrlFormat(String ip, int port) {
        return ip + ":" + port;
    }

    /**
     * map 형태의 kafka config
     *
     * @return map 형태의 kafka 정보
     */
    public Map<String, String> getKafkaConfig() {
        return kafkaConfig;
    }
}
