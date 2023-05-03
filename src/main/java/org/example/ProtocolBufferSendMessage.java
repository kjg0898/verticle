package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProtocolBufferSendMessage extends AbstractVerticle {

    public static final String KEY_SHARED_DATA_NET_SOCKET = "localmap.netsocket.test";
    private static final Logger logger = LoggerFactory.getLogger(ProtocolBufferSendMessage.class);
    private static final String FILE_NAME = "log_1.bin";
    private static final String READPATH = "./src/main/resources/protocolBuffer/" + FILE_NAME;

    public static void main(String[] args) {

        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(vertxOptions, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(new ProtocolBufferSendMessage());
                logger.info("Clustered vertx instance started successfully!");
            } else {
                logger.info("Clustered vertx instance failed to start!");
            }
        });
    }

    @Override
    public void start(Promise<Void> startPromise)   {

        LocalMap<String, Buffer> localMap = vertx.sharedData().getLocalMap(KEY_SHARED_DATA_NET_SOCKET);
        vertx.fileSystem().readFile(READPATH, ar -> {
            if (ar.succeeded()) {
                Buffer fileBuffer = ar.result();
                LineIterator it = IOUtils.lineIterator(new ByteArrayInputStream(fileBuffer.getBytes()), StandardCharsets.UTF_8);
                AtomicBoolean allMessagesSent = new AtomicBoolean(false);
                startPromise.complete();
        vertx.setPeriodic(2000, id -> {
            if (it.hasNext()) {
                String readProtoBfBody = it.nextLine();
                    JsonObject messageJsonObject = new JsonObject().put("verticlename", "ProtocolBufferSendMessage").put("body", readProtoBfBody);
                    Buffer protobuffer = Buffer.buffer(String.valueOf(messageJsonObject));
                    vertx.eventBus().publish("dev-Bus", protobuffer);
                    localMap.put("message", protobuffer);
                    logger.info(" message : {}", protobuffer);
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
            }  else {
                startPromise.fail(ar.cause());
            }
        });
    }

    public BufferedReader readFile(String readPath) throws IOException {
        File file = new File(readPath);
        return new BufferedReader(new FileReader(file));
    }
}