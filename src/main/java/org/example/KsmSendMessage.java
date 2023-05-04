package org.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.example.config.ksm.KsmConfig;
import org.example.ksm.code.CommandCode;
import org.example.ksm.code.InfoCode;
import org.example.ksm.coder.KSMDecoder;
import org.example.ksm.coder.KSMEncoder;
import org.example.ksm.dto.KSMFrame;
import org.example.ksm.dto.KSMFrameBody;
import org.example.ksm.dto.KSMFrameHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.CRC32;


public class KsmSendMessage extends AbstractVerticle {

    public static final String KEY_SHARED_DATA_NET_SOCKET = "localmap.netsocket.test";
    private static Logger logger = LoggerFactory.getLogger(KsmSendMessage.class);

   /* private static final String FILENAME = "test_ice";
    private static final String CARKEY = "NAVY_AUTO_VC_01";
    private static final String READPATH = "./src/main/resources/ksm/" + FILENAME + ".txt";*/

    private Integer cmdCode;

    public static void main(String[] args) {
        ClusterManager clusterManager = new HazelcastClusterManager();
        VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);

        Vertx.clusteredVertx(vertxOptions, res -> {
            if (res.succeeded()) {
                Vertx vertx = res.result();
                vertx.deployVerticle(new KsmSendMessage());
                logger.info("Clustered vertx instance started successfully!");

            } else {
                logger.info("Clustered vertx instance failed to start!");
            }
        });

    }

    @Override
    public void start(Promise<Void> startPromise) {

        //JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getAgvConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBasisConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisDetectorConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisDetector2Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisEvent1Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisEvent2Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisEvent3Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisEvent4Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisEvent5Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisEvent6Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisEvent7Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisIceConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisIce2Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getBisIce3Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getChargeConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getControlConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getDamConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getDetectorConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getEventConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getIceConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getIce1Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getIce2Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getIce3Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getIce4Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getLdmIce10Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getNavy_auto_iceConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getNavy_controlConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getNavy_detectorConfig()).toJsonObject();
 JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getNavy_detector2Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getNavy_eventConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getNavy_mexus_iceConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getPev1Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getPev2Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getPev3Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getPev4Config()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getPevConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getSaimConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getSensorConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getTest_iceConfig()).toJsonObject();
// JsonObject Config = vertx.fileSystem().readFileBlocking(KsmConfig.getTrafficConfig()).toJsonObject();



        String FILENAME = Config.getString("FILENAME"),
                CARKEY = Config.getString("CARKEY"),
                READPATH = Config.getString("READPATH");

        KSMFrame ksmFrame = new KSMFrame();
        LocalMap<String, Buffer> localMap = vertx.sharedData().getLocalMap(KEY_SHARED_DATA_NET_SOCKET);
        vertx.fileSystem().readFile(READPATH, ar -> {
            if (ar.succeeded()) {
                Buffer fileBuffer = ar.result();
                LineIterator it = IOUtils.lineIterator(new ByteArrayInputStream(fileBuffer.getBytes()), StandardCharsets.UTF_8);
                AtomicBoolean allMessagesSent = new AtomicBoolean(false);
                startPromise.complete();
                vertx.setPeriodic(2000, id -> {
                    if (it.hasNext()) {
                        String readKsmBody = it.nextLine();
                        Buffer ksmBuffer = encode(makeKSMFrame(readKsmBody, CARKEY, getInfoCode(FILENAME)));
                        ksmFrame.decode(ksmBuffer, new KSMDecoder());
                        JsonObject messageJsonObject = new JsonObject().put("verticlename", "KsmSendMessage").put("filename", FILENAME).put("body", ksmFrame);
                        Buffer ksmSendProduCer = messageJsonObject.toBuffer();
                        vertx.eventBus().publish("dev-Bus", ksmSendProduCer);
                        localMap.put("message", ksmSendProduCer);
                        logger.info(String.valueOf(ksmSendProduCer));
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
            } else {
                startPromise.fail(ar.cause());
            }
        });

    }

    @Override
    public void stop() {
        vertx.setTimer(1000, id -> {
            vertx.close();
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
        if (accessKey == null) {
            accessKey = "";
        }

        String createTime = sdf.format(System.currentTimeMillis());
        if (infoCode == 0 || infoCode == 801) {
            if (cmdCode == null) {
                cmdCode = CommandCode.REQUEST_CAR.getCode();
            } else {
                cmdCode = CommandCode.SEND_FROM_CAR.getCode();
            }
        } else if (infoCode == 100) {
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
        byte[] crcCheckByte = ksmFrame.encode(new KSMEncoder()).getBytes();
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
}

