package com.johnmcdnl.nodaireacht;

public class Nodaireacht {
    private String jsonStr;

    public Nodaireacht(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public Result get(String key) {
        return new Result(jsonStr).get(key);
    }

    public String string(String key) {
        return new Result(jsonStr).string(key);
    }

    public boolean asBoolean(String key) {
        return Boolean.parseBoolean(string(key));
    }

    public Double number(String key) {
        return Double.parseDouble(string(key));
    }

    public Object asNull(String key) {
        if (get(key).isNull()) {
            return null;
        }
        throw new RuntimeException("Value is not null");
    }
}
