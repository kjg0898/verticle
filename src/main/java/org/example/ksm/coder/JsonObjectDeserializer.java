package org.example.ksm.coder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.util.Map;

/**
 * JsonObject Deserializer
 *
 * NOTE :
 * @author sojeong
 * @since 2019.11.08
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 * 수정일        수정자          수정내용
 * ------------ -------------- --------------------------
 * 2019.11.08   sojeong         최초 생성
 *
 */
public class JsonObjectDeserializer extends StdDeserializer<JsonObject> {

    private final TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>() {};

    /**
     * 생성자
     */
    public JsonObjectDeserializer() {
        this(null);
    }

    /**
     * 생성자
     */
    public JsonObjectDeserializer(final Class<?> type) {
        super(type);
    }

    /**
     * Deserialize
     *
     * @param jsonParser -  parse json context
     * @param context - context
     * @return jsonObject
     * @throws IOException
     */
    @Override
    public JsonObject deserialize(final JsonParser jsonParser, final DeserializationContext context) throws IOException {
        final ObjectCodec objectCodec = jsonParser.getCodec();
        final Map<String, Object> value = objectCodec.readValue(jsonParser, type);

        return new JsonObject(value);
    }
}