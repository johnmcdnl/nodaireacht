package com.johnmcdnl.gjson;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;


@SuppressWarnings({"WeakerAccess", "unused"})
public class Result {

    private ResultType resultType;

    private JsonElement jsonElement;

    private JsonObject jsonObject;
    private JsonArray jsonArray;
    private JsonNull jsonNull;
    private JsonPrimitive jsonPrimitive;

    private String jsonStr;

    public Result(String jsonStr) {
        this.jsonStr = jsonStr;
        this.resultType = ResultType.Undefined;
        this.parseJSON();
    }

    private Result(JsonElement element) {
        this.jsonElement = element;
        this.resultType = ResultType.Undefined;
        this.parseJSON();
    }


    private Result parseJSON() {
        if (this.jsonElement == null) {
            this.jsonElement = new Gson().fromJson(this.jsonStr, JsonElement.class);
        }

        if (this.jsonElement instanceof JsonArray) {
            this.resultType = ResultType.Array;
            this.jsonArray = (JsonArray) this.jsonElement;
        }

        if (this.jsonElement instanceof JsonObject) {
            this.resultType = ResultType.Object;
            this.jsonObject = (JsonObject) this.jsonElement;
        }

        if (this.jsonElement instanceof JsonPrimitive) {
            this.resultType = ResultType.Primitive;
            this.jsonPrimitive = (JsonPrimitive) this.jsonElement;
        }

        if (this.jsonElement instanceof JsonNull) {
            this.resultType = ResultType.Null;
            this.jsonNull = (JsonNull) this.jsonElement;
        }

        return this;
    }

    private Result parseJSON(String jsonStr) {
        this.jsonStr = jsonStr;
        return this.parseJSON();
    }

    public Result get(String key) {

        String[] keySegments = getKeySegments(key);

        for (int i = 0; i < keySegments.length; i++) {
            boolean isLast = i == keySegments.length - 1;
            key = retrieveKey(keySegments[i]);

            switch (this.resultType) {
                case Object:
                    this.jsonElement = this.jsonObject.get(key);
                    break;
                case Array:
                    if (StringUtils.isNumeric(key)) {
                        this.jsonElement = this.jsonArray.get(Integer.parseInt(key));
                    }

                    if (Objects.equals(key, "#")) {
                        if (isLast) {
                            this.jsonElement = new JsonPrimitive(this.jsonArray.size());
                            break;
                        }
                        continue;

                    }

                    break;
                case Primitive:
                    break;
                case Null:
                    break;
                case Undefined:
                    break;
            }
            this.parseJSON();
        }

        return this.parseJSON();
    }

    private String retrieveKey(String keyValue) {

        String finalKey = keyValue;

        if (keyValue.contains("*") || keyValue.contains("?")) {
            Pattern pattern = Pattern.compile(keyValue.replace("", ".").replace("?", ".?"));
            Set<?> keys = jsonObject.keySet();
            List<String> candidates = new ArrayList<>();
            keys.forEach(key -> {
                if (key.toString().matches(pattern.pattern())) {
                    candidates.add(key.toString());
                }
            });

            if (candidates.size() != 1) {
                throw new RuntimeException(
                        "Didn't find exactly 1 potential match for key: "
                                + keyValue + " --- "
                                + Arrays.toString(candidates.toArray())
                );
            }
            finalKey = candidates.get(0);
        }

        return finalKey;
    }

    private String[] getKeySegments(String key) {
        final String ESCAPED_BACKSLASH_DOT = "{{ESCAPED_BACKSLASH_DOT}}";

        key = key.replaceAll("\\\\.", ESCAPED_BACKSLASH_DOT);

        String[] segments = StringUtils.split(key, ".");
        for (int i = 0; i < segments.length; i++) {
            segments[i] = segments[i].replace(ESCAPED_BACKSLASH_DOT, ".");
        }

        return segments;
    }

    public String string(String key) {
        return get(key).asString();
    }

    public String asString() {
        switch (this.resultType) {

            case Object:
                return this.jsonObject.toString();
            case Array:
                return this.jsonArray.toString();
            case Primitive:
                return this.jsonPrimitive.getAsString();
            case Null:
                return JsonNull.INSTANCE.toString();
            case Undefined:
                break;
        }

        return "UNSUPPORTED " + this.resultType;
    }

    @Override
    public String toString() {
        return "Result{" +
                "resultType=" + resultType +
                ", jsonElement=" + jsonElement +
                ", jsonObject=" + jsonObject +
                ", jsonArray=" + jsonArray +
                ", jsonPrimitive=" + jsonPrimitive +
                ", jsonNull=" + jsonNull +
                ", jsonStr='" + jsonStr + '\'' +
                '}';
    }

    public List<String> array() {
        List<String> elements = new ArrayList<>();
        this.jsonArray.forEach(jsonElement -> elements.add(new Result(jsonElement).asString()));

        return elements;
    }

    public boolean isNull() {
        return this.resultType == ResultType.Null &&
                this.jsonElement.isJsonNull() &&
                this.jsonNull == JsonNull.INSTANCE;
    }
}
