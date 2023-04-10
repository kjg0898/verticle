package org.example.ksm;/*
package org.example.ksm;


import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.example.ksm.collect.AbstractKafkaCollectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


*/
/**
 * 시설물 Template Verticle
 *
 * NOTE :
 * @author sojeong
 * @since 2019.11.20
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.20   sojeong         최초 생성
 *
 *//*

public abstract class AbstractKSMFacilityTemplateVerticle extends AbstractKSMTemplateVerticle {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKSMFacilityTemplateVerticle.class);
    protected AbstractRedisCollectSimpleService redisCollectService;
    protected AbstractKafkaCollectService kafkaCollectService;


    */
/**
     * send message to kxmessage verticle and collect message
     *
     * @param message - facility ksmframe
     * @return future result
     *//*

    @Override
    protected Promise<Void> handle(Message<JsonObject> message) {
        Promise<Void> promise = Promise.promise();

        //데이터 전송
        getServiceAddress(getKXMMessageSourceName()).future().compose(kRecord -> {
            if (kRecord != null) {
                //KXMessage 전송
                vertx.eventBus().send(kRecord.getLocation().getString("endpoint"), message.body() */
/*getKXMData(message.body())*//*
);
                logger.debug("Send KSM To {} : {}", getKXMMessageSourceName(), message.body() */
/*getKXMData(message.body())*//*
);
            } else {
                logger.debug("Not Found {} Address", getKXMMessageSourceName());
            }
            return Future.succeededFuture();
        }).onComplete(ar -> {
            redisCollectService.start(message.body()).onComplete(handler -> {
                if (handler.succeeded()) {
                    kafkaCollectService.start(message.body(), promise.future());
                } else {
                    promise.future().failed();
                }
            });
        });

        return promise;
    }

    */
/**
     * get service address
     *
     * @param serviceName - 서비스명
     * @return record
     *//*

    protected Promise<Record> getServiceAddress(String serviceName) {
        Promise<Record> promise = Promise.promise();
        discovery.getRecord(name -> name.getName().equals(serviceName), promise);
        return promise;
    }

    */
/**
     * get KXM Message Source Name
     *
     * @return KXM Message Source Name
     *//*

    abstract protected String getKXMMessageSourceName();

    */
/**
     * get ksmframe message format
     *
     * @param message - facility ksmframe
     * @return ksmframe message format
     *//*

    abstract protected JsonObject getKXMData(JsonObject message);


}
*/
