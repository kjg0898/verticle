package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ProtocolBufferSendMessage extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolBufferSendMessage.class);
    public static final String KEY_SHARED_DATA_NET_SOCKET = "localmap.netsocket.test";
    private static final String FILE_NAME = "log_1.bin";
    private static final String FILE_PATH = "./src/main/resources/protocolBuffer/" + FILE_NAME;
    public static void main(String[] args){

        /*Vertx vertx = Vertx.vertx();
        ProtocolBufferSendMessage prm = new ProtocolBufferSendMessage();
        vertx.deployVerticle(prm, res -> {
            if (res.succeeded()) {
                logger.info("ProtocolBufferSendMesseage deployed successfully!");
            } else {
                logger.info("ProtocolBufferSendMesseage deployment failed!",res.cause());
            }
        });*/

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
    public void start() throws IOException {


        LocalMap<String, Buffer> localMap = vertx.sharedData().getLocalMap(KEY_SHARED_DATA_NET_SOCKET);
        BufferedReader reader = readFile(FILE_PATH);

        vertx.setPeriodic(2000, id -> {
            try {
                String readProtoBfBody = reader.readLine();
                if (readProtoBfBody != null) {
                    Buffer protobuffer = Buffer.buffer(readProtoBfBody);
                    localMap.put("message", protobuffer);
                    vertx.eventBus().publish("dev-Bus", protobuffer);
                    logger.info(String.valueOf(protobuffer));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            localMap.keySet().stream().forEach(netSocketId -> {
                logger.info("localMap remove : {}", netSocketId);
                localMap.remove(netSocketId);
            });
        });

   /*     String fileName = "log_1";
        String readPath = "./src/main/resources/protocolBuffer/" + fileName + ".bin";
        BufferedReader reader = readFile(readPath);
        NetClient netClient = vertx.createNetClient();

        vertx.setPeriodic(2000, timerId -> {
            final String readProtoBfBody;
            try {
                readProtoBfBody = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(readProtoBfBody != null) {
                Buffer protobuffer = Buffer.buffer(readProtoBfBody);
                byte[] message = protobuffer.getBytes();
                    if (localMap.isEmpty()) {
                        netClient.connect(1234, "localhost", res -> {
                            if (res.succeeded()) {
                                NetSocket netSocket = res.result();
                                netSocket.write(Arrays.toString(message));
                                logger.info("connect success");
                                logger.info("protobuffer :  {}", message);
                            } else if (res.failed()) {
                                // Handle the case where the connection failed
                                logger.error("Failed to connect: {}", res.cause().getMessage());
                                // Retry the connection after a delay
                                vertx.setTimer(5000, timer2Id -> {
                                    logger.info("Retrying connection...");
                                    // Attempt to connect again
                                    netClient.connect(1234, "localhost", res2 -> {
                                        if (res2.succeeded()) {
                                            NetSocket netSocket = res2.result();
                                            netSocket.write(Arrays.toString(message));
                                        } else {
                                            // Handle the case where the connection fails again
                                            logger.error("Failed to connect after retry: {}", res2.cause().getMessage());
                                        }
                                    });
                                });
                            } else {
                                logger.info("LocalMap : {}", localMap);
                                EventBus bus = vertx.eventBus();
                                bus.consumer("myVerticle", EventBusmessage -> logger.info("EventBus protobuffer :  {}",message));
                            }
                        *//*    sleep(1500);*//*
                        });
                        localMap.keySet().forEach(netSocketId -> {
                            logger.info("localMap remove : {}", netSocketId);
                            localMap.remove(netSocketId);
                        });
                    }
                }
            });*/
    }
    public BufferedReader readFile(String readPath) throws IOException {
        File file = new File(readPath);
        return new BufferedReader(new FileReader(file));
    }
    /*private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

}