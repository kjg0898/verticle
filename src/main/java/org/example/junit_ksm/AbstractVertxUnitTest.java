package org.example.junit_ksm;

import io.reactivex.functions.Action;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Function;

@RunWith(VertxUnitRunner.class)
public abstract class AbstractVertxUnitTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractVertxUnitTest.class);

    protected Vertx vertx;
    protected JsonObject config;

    private Path defaultConfigFilePath;
    private String profileKey = "vertx.profile.active";

    /**
     * 초기화
     *
     * @param context - context
     */
    @Before
    public void init(TestContext context) {
        this.vertx = Vertx.vertx();
        defaultConfigFilePath = Paths.get("./src/main/resources/application.json");
        try {
            config = readConfig(defaultConfigFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * close vertx
     *
     * @param context - context
     */
    @After
    public void afterFinish(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    /**
     * read config
     *
     * @param path - path
     * @return config
     */
    private JsonObject readConfig(Path path) {
        JsonObject conf = readJsonObject(path);
        if (conf != null) {
            if (!conf.containsKey(profileKey)) {
                return conf;
            }

            // profile로 설정된 파일을 추가로 로딩하여 기본 객체에 Merge
            String activeProfileName = conf.getString(profileKey);
            Path parent = path.getParent();
            String active = getPrefixConfigFileName(path.getFileName(), activeProfileName);
            Path resolvePath = parent.resolve(active);
            conf.mergeIn(readJsonObject(resolvePath));
        }
        return conf;
    }

    /**
     * read config
     *
     * @param path - path
     * @return config
     */
    private JsonObject readJsonObject(Path path) {
        FileInputStream fis = null;
        Scanner scanner = null;
        try {
            fis = new FileInputStream(path.toFile());

            if( fis != null ) {
                scanner = new Scanner(fis, "UTF-8").useDelimiter("\\A");
                String read = scanner.next();
                return new JsonObject(read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if( scanner != null ) {
                scanner.close();
            }

            if( fis != null ) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("{}", e);
                }
            }
        }
        return null;
    }

    /**
     * get file Name
     *
     * @param fileName - 파일명
     * @param active - profile명
     * @return 파일명
     */
    private String getPrefixConfigFileName(Path fileName, String active) {
        return fileName.toString().split("\\.")[0] + "-" + active + ".json";
    }

    protected <T> void readTestDataFromFile(TestContext context, String testFilePath, Function<JsonObject, Future<T>> caller, Action complete) throws IOException {
        File file = new File(testFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        long lineCount = Files.lines(file.toPath()).count();
        Async async = context.async((int) lineCount);
        String data;
        while ((data = reader.readLine()) != null) {
            if( data.length() == 0 ) continue;
            JsonObject message = new JsonObject(data);
            Future<T> result = caller.apply(message);


            // 이부분 생각해 볼 필요가 있음.
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        async.handler(ch -> {
            if( complete != null ) {
                try {
                    reader.close();
                    complete.run();
                } catch (Exception e) {
                    // do Nothing
                    e.printStackTrace();
                }
            }
        });
//            cleanTestDataToRedis(redisBSMSelectLinkInfo.getRedisClient());
    }

    protected <T> void readTestDataFromFile(TestContext context, String testFilePath, Function<JsonObject, Future<T>> caller ) throws IOException {
        this.readTestDataFromFile(context, testFilePath, caller, null);
    }
}

