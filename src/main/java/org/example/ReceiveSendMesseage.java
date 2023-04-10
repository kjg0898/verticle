package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class ReceiveSendMesseage extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(ReceiveSendMesseage.class);

    public static void main(String[] args){
       /* Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ReceiveSendMesseage(), res -> {
            if (res.succeeded()) {
                logger.info("ReceiveSendMesseage deployed successfully!");
            } else {
                logger.info("ReceiveSendMesseage deployment failed!",res.cause());
            }
        });*/

        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(vertxOptions, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(new ReceiveSendMesseage());
                logger.info("Clustered vertx instance started successfully!");
            } else {
                logger.info("Clustered vertx instance failed to start!");
            }
        });

    }

    public static final String KEY_SHARED_DATA_NET_SOCKET = "localmap.netsocket.test";
    @Override
    public void start() throws IOException {

     /*   NetServer server = vertx.createNetServer();

        NetClient netclient = vertx.createNetClient();*/



        vertx.eventBus().consumer("any-Bus", message -> {
            Object body = message.body();
            Buffer buffer;
            if (body instanceof Buffer) {
                buffer = (Buffer) body;
                logger.info("Received buffer message: {}", buffer);
            }  else if (body instanceof String messageString) {
                buffer = Buffer.buffer(messageString);
                logger.info("Received string message: {}", messageString);
            }  else if (body instanceof byte[] messageBytes) {
                buffer = Buffer.buffer(messageBytes);
                logger.info("Received byte message: {}", Arrays.toString(messageBytes));
            }  else {
                buffer = null;
                logger.warn("Received message with unexpected type: {}", body);
            }

            LocalMap<String, Buffer> localMap = vertx.sharedData().getLocalMap(KEY_SHARED_DATA_NET_SOCKET);
            vertx.setPeriodic(2000, id -> {

                    if (buffer != null) {
                        localMap.put("message", buffer);
                        vertx.eventBus().publish("dev-Bus", buffer);
                        logger.info(String.valueOf(buffer));
                    }
                    localMap.keySet().stream().forEach(netSocketId -> {
                        logger.info("localMap remove : {}", netSocketId);
                        localMap.remove(netSocketId);
                    });
            });
                });
      /*  server.connectHandler(socket ->{
            socket.handler(buffer ->{
                logger.info("handle success");
                logger.info("Received message : {}", buffer);
                Buffer myBuffer = buffer;

                if(myBuffer != null) {
                    if (localMap.isEmpty()) {
                        netclient.connect(1234, "localhost", res -> {
                            if (res.succeeded()) {
                                logger.info("connect success");
                                logger.info("myBuffer :  {}", myBuffer);
                                NetSocket netSocket = res.result();
                                netSocket.write(myBuffer);

                            } else if (res.failed()) {
                                // Handle the case where the connection failed
                                logger.error("Failed to connect: {}", res.cause().getMessage());
                                // Retry the connection after a delay
                                vertx.setTimer(5000, timerId -> {
                                    logger.info("Retrying connection...");
                                    // Attempt to connect again
                                    netclient.connect(1234, "localhost", res2 -> {
                                        if (res2.succeeded()) {
                                            NetSocket netSocket = res2.result();
                                            netSocket.write(myBuffer);
                                        } else {
                                            // Handle the case where the connection fails again
                                            logger.error("Failed to connect after retry: {}", res2.cause().getMessage());
                                        }
                                    });
                                });
                            } else {
                                logger.info("LocalMap : {}", localMap);
                                EventBus bus = vertx.eventBus();
                                bus.consumer("myVerticle", EventBusmessage -> {
                                    logger.info("myBuffer :  {}",myBuffer);
                                });
                            }
                            sleep(1500);
                        });
                        localMap.keySet().stream().forEach(netSocketId -> {
                            logger.info("localMap remove : {}", netSocketId);
                            localMap.remove(netSocketId);
                        });
                    }
                }
            });
        });
        server.listen(5678,"localhost", res -> {
            if (res.succeeded()) {
                logger.info("ReceiveMesseage server listen successfully!");
            } else {
                logger.info("ReceiveMesseage server listen failed!");
            }
        });*/
    }

  /*  private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
}