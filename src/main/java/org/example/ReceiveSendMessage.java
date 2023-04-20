package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReceiveSendMessage extends AbstractVerticle {

    public static final String KEY_SHARED_DATA_NET_SOCKET = "localmap.netsocket.test";
    private static final Logger logger = LoggerFactory.getLogger(ReceiveSendMessage.class);

    public static void main(String[] args) {

        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(vertxOptions, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(new ReceiveSendMessage());
                logger.info("Clustered vertx instance started successfully!");
            } else {
                logger.info("Clustered vertx instance failed to start!");
            }
        });

    }

    @Override
    public void start() throws IOException {
        AtomicBoolean allMessagesSent = new AtomicBoolean(false);

        vertx.eventBus().consumer("any-Bus", message -> {
            Object body = message.body();
            Buffer buffer;
            if (body instanceof Buffer) {
                buffer = (Buffer) body;
                logger.info("Received buffer message: {}", buffer);
            } else if (body instanceof String messageString) {
                buffer = Buffer.buffer(messageString);
                logger.info("Received string message: {}", messageString);
            } else if (body instanceof byte[] messageBytes) {
                buffer = Buffer.buffer(messageBytes);
                logger.info("Received byte message: {}", Arrays.toString(messageBytes));
            } else {
                buffer = null;
                logger.warn("Received message with unexpected type: {}", body);
            }

            LocalMap<String, Buffer> localMap = vertx.sharedData().getLocalMap(KEY_SHARED_DATA_NET_SOCKET);
            vertx.setPeriodic(2000, id -> {

                if (buffer != null) {
                    JsonObject messageJsonObject = new JsonObject().put("verticlename", "ReceiveSendMessage").put("body", buffer);
                    Buffer recievedBuffer = Buffer.buffer(String.valueOf(messageJsonObject));
                    localMap.put("message", recievedBuffer);
                    vertx.eventBus().publish("dev-Bus", recievedBuffer);
                    logger.info(String.valueOf(recievedBuffer));
                } else {
                    // 모든 메시지가 전송되었음을 표시
                    localMap.clear();
                    allMessagesSent.set(true);
                    vertx.setPeriodic(1000, stopid -> {
                        if (allMessagesSent.get() && localMap.isEmpty()) {
                            // 모든 메시지가 전송되었고, 전송 대기 중인 메시지도 없음
                            logger.info("All messages sent, stopping the sender");
                            vertx.cancelTimer(stopid);
                            try {
                                stop();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            });
        });
    }
}