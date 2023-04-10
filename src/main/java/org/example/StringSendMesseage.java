package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class StringSendMesseage extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(StringSendMesseage.class);
    public static final String KEY_SHARED_DATA_NET_SOCKET = "localmap.netsocket.test";

    public static void main(String[] args){
         /*Vertx vertx = Vertx.vertx();
        StringSendMesseage prm = new StringSendMesseage();
        vertx.deployVerticle(prm, res -> {
            if (res.succeeded()) {
                logger.info("StringSendMesseage deployed successfully!");
            } else {
                logger.info("StringSendMesseage deployment failed!",res.cause());
            }
        });*/

        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(vertxOptions, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(new StringSendMesseage());
                logger.info("Clustered vertx instance started successfully!");
            } else {
                logger.info("Clustered vertx instance failed to start!");
            }
        });
    }


    @Override
    public void start() {
        LocalMap<String, String> localMap = vertx.sharedData().getLocalMap(KEY_SHARED_DATA_NET_SOCKET);

        vertx.setPeriodic(2000, id -> {
                    String message = "Hello, world!";
                    localMap.put("message", message);
                    vertx.eventBus().publish("dev-Bus", message);
                    logger.info(message);
                });
            localMap.keySet().stream().forEach(netSocketId -> {
                logger.info("localMap remove : {}", netSocketId);
                localMap.remove(netSocketId);
            });
        }
    }



       /* // Get a cluster-wide event bus
        EventBus eventBus = vertx.eventBus().getDelegate();

        // Send a message to a "ProducerVerticle" in another project
        eventBus.send("producer-address", "Some message", res -> {
            if (res. {
                logger.info("Message sent successfully to producer!");
            } else {
                logger.error("Failed to send message to producer", res.cause());
            }
        });*/





/*        NetClient netClient = vertx.createNetClient();

        vertx.setPeriodic(2000, timerId -> {
        if (message != null) {
            if (localMap.isEmpty()) {
                netClient.connect(1234, "localhost", res -> {
                    if (res.succeeded()) {
                        NetSocket netSocket = res.result();
                        netSocket.write(message);
                        logger.info("String message :  {}", message);
                    } else if (res.failed()) {
                        // Handle the case where the connection failed
                        logger.error("Failed to connect: {}", res.cause().getMessage());
                        // Retry the connection after a delay
                        vertx.setPeriodic(5000, timer2Id -> {
                            logger.info("Retrying connection...");
                            // Attempt to connect again
                            netClient.connect(1234, "localhost", res2 -> {
                                if (res2.succeeded()) {
                                    NetSocket netSocket = res2.result();
                                    netSocket.write(message);
                                } else {
                                    // Handle the case where the connection fails again
                                    logger.error("Failed to connect after retry: {}", res2.cause().getMessage());
                                }
                            });
                        });
                    } else {
                        logger.info("LocalMap : {}", localMap);
                        EventBus bus = vertx.eventBus();
                        bus.consumer("myVerticle", EventBusMessage -> {
                            logger.info("EventBus String message :  {}", EventBusMessage);
                        });
                    }
                });
            }
        }
    });*/

