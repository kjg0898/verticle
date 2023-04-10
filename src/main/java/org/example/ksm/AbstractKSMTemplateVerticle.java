package org.example.ksm;/*
package org.example.ksm;


import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.stream.Collectors;

*/
/**
 * KSM Template Verticle
 *
 * NOTE :
 * @author htkim
 * @since 2019.11.12
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.12    htkim         최초 생성
 *
 *//*

public abstract class AbstractKSMTemplateVerticle extends BaseMicroserviceVerticle {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKSMTemplateVerticle.class);

    */
/**
     * send message to plugin verticle and
     * @param startFuture
     * @throws Exception
     *//*

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        super.start();

        // EventBus 코덱 등록.
        try {
            vertx.eventBus().registerDefaultCodec(TargetVehicleMessage.class, new TargetVehicleMessageCodec());
        } catch (IllegalStateException e) {
            logger.info("registerDefaultCodec exception: {}", e);
        }

        initStart();

       publishMessageSource(getMessageSourceName(), getMessageSourceAddress());

        MessageSource.<JsonObject>getConsumer(discovery, new JsonObject().put("name", getMessageSourceName()), ar -> {
            if (ar.succeeded()) {
                MessageConsumer<JsonObject> consumer = ar.result();
                consumer.handler(message -> {
                    String action = message.headers().get("action");
                    if( "healthCheck".equals(action) ) {
                        // Health Check 로직 구현.
                        message.reply("pong");
                        return;
                    }

                    String uuid = UUID.randomUUID().toString();

                    // 집계용 로그를 남김.(처리시작)
                    //TotalLog.totalLog(MessageUtil.createLogStartMessage(uuid, getName(), message.body()));

                    Promise<Void> promise = handle(message);

                    promise.future().onComplete(next -> {
                        // 집계용 로그를 남김.(처리종료)
                        //TotalLog.totalLog(MessageUtil.createLogEndMessage(uuid, getName(), next));

                        discovery.getRecords(jo -> jo.getName().startsWith(getMessageSourcePluginName()), arPlugin -> {
                            if (arPlugin.succeeded()) {
                                arPlugin.result().stream().collect(Collectors.groupingBy(record -> record.getName()))
                                        .forEach((k, v) -> {
                                            String address = v.stream().findAny().get().getLocation().getString("endpoint");
                                            vertx.eventBus().send(address, message.body());
                                            if (logger.isDebugEnabled()) {
                                                logger.debug(LogFormat.keyValueFormat("Send to ", address));
                                            }
                                        });
                            }
                        });
                    });

                });
                startFuture.complete();
            } else {
                startFuture.fail("Can not access consumer. class: " + this.getClass().getName());
            }
        });
    }

    */
/**
     * initialize service
     *//*

    abstract protected void initStart();

    */
/**
     * get module name
     *
     * @return name
     *//*

    abstract protected String getName();

    */
/**
     * get message source name
     *
     * @return message source name
     *//*

    abstract protected String getMessageSourceName();

    */
/**
     * get message source address
     *
     * @return message source address
     *//*

    abstract protected String getMessageSourceAddress();

    */
/**
     * get message source plugin name
     *
     * @return message source plugin name
     *//*

    abstract protected String getMessageSourcePluginName();

    */
/**
     * handle message
     * @param message
     * @return
     *//*

    abstract protected Promise<Void> handle(Message<JsonObject> message);

}
*/
