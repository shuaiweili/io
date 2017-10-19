package com.git.lee.io.aio.metrics;

import com.git.lee.io.aio.metrics.utils.ApiUtil;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LISHUAIWEI
 * @date 2017/10/19 11:08
 */
public class Metrics {

    private String name;
    private long timestamp;
    private Map<String, String> tags;
    private Map<String, Double> fields;

    public Metrics(String name, long timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public void addTag(String key, String value) {
        if (tags == null) {
            tags = new HashMap<>();
        }
        tags.put(key, value);
    }

    public void addField(String key, Double value) {
        if (fields == null) {
            fields = new HashMap<>();
        }
        fields.put(key, value);
    }

    public static Metrics readFrom(ByteBuffer buffer) {
        String name = ApiUtil.readShortString(buffer);
        if (name == null) return null;
        Metrics metrics = new Metrics(name, buffer.getLong());
        int tagSize = buffer.get();
        for (int i = 0; i < tagSize; i++) {
            metrics.addTag(ApiUtil.readShortString(buffer), ApiUtil.readShortString(buffer));
        }
        int fieldSize = buffer.get();
        for (int i = 0; i < fieldSize; i++) {
            metrics.addField(ApiUtil.readShortString(buffer), buffer.getDouble());
        }
        return metrics;
    }

    public void writeTo(ByteBuffer buffer) {
        ApiUtil.writeShortString(buffer, name);
        buffer.putLong(timestamp);
        if (tags != null) {
            buffer.put((byte)tags.size());
            for (Map.Entry<String, String> entry : tags.entrySet()) {
                ApiUtil.writeShortString(buffer, entry.getKey());
                ApiUtil.writeShortString(buffer, entry.getValue());
            }
        } else {
            buffer.put((byte)0);
        }

        if (fields != null) {
            buffer.put((byte)fields.size());
            for (Map.Entry<String, Double> entry : fields.entrySet()) {
                ApiUtil.writeShortString(buffer, entry.getKey());
                buffer.putDouble(entry.getValue());
            }
        } else {
            buffer.put((byte)0);
        }
    }

    public int sizeInBytes() {
        int size = ApiUtil.shortStringLength(name)
                    + 8   //timestamp length
                    + 1   //tags size
                    + 1;  //fields size
        if (tags != null) {
            for (Map.Entry<String, String> entry : tags.entrySet()) {
                size += ApiUtil.shortStringLength(entry.getKey());
                size += ApiUtil.shortStringLength(entry.getValue());
            }
        }
        if (fields != null) {
            for (Map.Entry<String, Double> entry : fields.entrySet()) {
                size += ApiUtil.shortStringLength(entry.getKey());
                size += 8; //field value
            }
        }
        return size;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public Map<String, Double> getFields() {
        return fields;
    }
}
