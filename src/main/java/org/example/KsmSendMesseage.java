package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.example.ksm.code.CommandCode;
import org.example.ksm.code.InfoCode;
import org.example.ksm.coder.KSMDecoder;
import org.example.ksm.coder.KSMEncoder;
import org.example.ksm.dto.KSMFrame;
import org.example.ksm.dto.KSMFrameBody;
import org.example.ksm.dto.KSMFrameHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.zip.CRC32;


public class KsmSendMesseage extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(KsmSendMesseage.class);
    public static final String KEY_SHARED_DATA_NET_SOCKET = "localmap.netsocket.test";
    private static final String FILENAME = "test-ice";
    private static final String CARKEY = "NAVY_AUTO_VC_01";
    private static final String READPAHT = "./src/main/resources/ksm/" + FILENAME + ".txt";

    private Integer cmdCode;

    public static void main(String[] args){
        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(vertxOptions, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(new KsmSendMesseage());
                logger.info("Clustered vertx instance started successfully!");
            } else {
                logger.info("Clustered vertx instance failed to start!");
            }
        });
    }

    @Override
    public void start() throws IOException {
        KSMFrame ksmFrame = new KSMFrame();
        LocalMap<String, Buffer> localMap = vertx.sharedData().getLocalMap(KEY_SHARED_DATA_NET_SOCKET);
        BufferedReader reader = readFile(READPAHT);
        vertx.setPeriodic(2000, id -> {
                try {
                    String readKsmBody = reader.readLine();
                    if (readKsmBody != null) {
                        Buffer ksmBuffer = encode(makeKSMFrame(readKsmBody, CARKEY, getInfoCode(FILENAME)));
                        logger.info(("encode message : {}"), ksmBuffer);
                        ksmFrame.decode(ksmBuffer, new KSMDecoder());
                        logger.info("Decode message : {}", ksmFrame);
                        Buffer buffer = Json.encodeToBuffer(ksmFrame);
                        localMap.put("message", buffer);
                        vertx.eventBus().publish("dev-Bus", buffer);

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


/*        LocalMap<String, String> localMap = vertx.sharedData().getLocalMap(KEY_SHARED_DATA_NET_SOCKET);
        NetClient netClient = vertx.createNetClient();
        String fileName = "test-ice";
        String carKey = "NAVY_AUTO_VC_01";
        String readPath = "./src/main/resources/ksm/" + fileName + ".txt";
        BufferedReader reader = readFile(readPath);
        vertx.setPeriodic(2000, timerId -> {
            final String readKsmBody; // declare readKsmBody as final
            try {
                readKsmBody = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (readKsmBody != null) {
                        Buffer ksmBuffer = encode(makeKSMFrame(readKsmBody, carKey, getInfoCode(fileName)));
                        *//*Buffer ksmBuffer =  Buffer.buffer(readKsmBody);*//*
                        if (localMap.isEmpty()) {
                            netClient.connect(1234, "localhost", res -> {
                                if (res.succeeded()) {
                                    NetSocket netSocket = res.result();
                                    netSocket.write(ksmBuffer);
                                    logger.info("connect success");
                                    logger.info("ksmBuffer :  {}", ksmBuffer);

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
                                                netSocket.write(ksmBuffer);
                                            } else {
                                                // Handle the case where the connection fails again
                                                logger.error("Failed to connect after retry: {}", res2.cause().getMessage());
                                            }
                                        });
                                    });
                                }else {
                                    logger.info("LocalMap : {}", localMap);
                                    EventBus bus = vertx.eventBus();
                                    bus.consumer("myVerticle", EventBusmessage -> {
                                        logger.info("EventBus ksmBuffer :  {}", ksmBuffer);
                                    });
                        }
                            });
                        }
                      *//*  sleep(2000);*//*
                }
        });*/
        localMap.keySet().stream().forEach(netSocketId -> {
            logger.info("localMap remove : {}", netSocketId);
            localMap.remove(netSocketId);
        });
    });
    }

    public BufferedReader readFile(String readPath) throws IOException {
        File file = new File(readPath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader;
    }

    public KSMFrame makeKSMFrame(String readKsmBody, String carKey, int infoCode) {
        KSMFrame ksmFrame = new KSMFrame();
        KSMFrameHeader header = new KSMFrameHeader();
        KSMFrameBody body = new KSMFrameBody();

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
        // 만료기간 없는 accessKey
        String accessKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjYXJLZXkiOiJ0ZXN0Q2FyIiwiaWF0IjoxNTcxOTc3OTE2fQ.R1CuqNUBayi3HxKrzDpBm3ds5bLSE0as_eUkTyrqWsc";
//        String accessKey= "";
//        accessKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjYXJLZXkiOiJ0ZXN0Q2FyIiwiaWF0IjoxNTc3OTI3Mjc2LCJleHAiOjE1Nzc5NDUyNzZ9.lGZipHTi6WNYnfLV49lXyPLysgcUMwtD9aeaGGtotwo";

        if(accessKey == null) {
            accessKey = "";
        }

        String createTime = sdf.format(System.currentTimeMillis());
        if (infoCode == 0 || infoCode == 801) {
            if(cmdCode == null) {
                cmdCode = CommandCode.REQUEST_CAR.getCode();
            } else {
                cmdCode = CommandCode.SEND_FROM_CAR.getCode();
            }
        } else if (infoCode == 100) {
            //if(cmdCode == null) {
            //    cmdCode = CommandCode.REQUEST_FACILITY.getCode();
            //} else {
            //    cmdCode = CommandCode.SEND_FROM_FACILITY.getCode();
            //}
            cmdCode = CommandCode.SEND_FROM_FACILITY.getCode();
        } else {
            cmdCode = CommandCode.SEND_FROM_PLATFORM.getCode();
        }
        int errCode = 0;
//        long bodyLen = 0;
        long bodyLen = readKsmBody.getBytes().length;
        long checkSum = 0L;

        header.setCarKey(carKey);
        header.setAccessKey(accessKey);
        header.setCreateTime(createTime);
        header.setCmdCode(cmdCode);
        header.setInfoCode(infoCode);
        header.setErrorCode(errCode);
        header.setCheckSum(checkSum);
        header.setBodyLength(bodyLen);

//        body.setBody(null);
        body.setBody(new JsonObject(readKsmBody));

        ksmFrame.setHeader(header);
        ksmFrame.setBody(body);
        //checksum
        CRC32 crc32 = new CRC32();
        byte [] crcCheckByte = ksmFrame.encode(new KSMEncoder()).getBytes();
        crc32.update(crcCheckByte);
        ksmFrame.getHeader().setCheckSum(crc32.getValue());
        return ksmFrame;
    }
    public Buffer encode(KSMFrame ksmFrame) {
        return ksmFrame.encode(new KSMEncoder());
    }

    private int getInfoCode(String fileName) {
        int infoCode;

        if (fileName.contains("control")) {
            infoCode = InfoCode.OFFER_COMMAND.getCode();
        } else if (fileName.contains("detector")) {
            infoCode = InfoCode.COLLECT_FACILITY.getCode();
        } else {
            infoCode = InfoCode.COLLECT_CAR.getCode();
        }
        return infoCode;
    }

    /*private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
    }

