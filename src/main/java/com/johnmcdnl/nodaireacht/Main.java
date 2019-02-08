package com.johnmcdnl.nodaireacht;

public class Main {

    private static final String jsonString = "{\n" +
            "  \"name\": {\"first\": \"Tom\", \"last\": \"Anderson\"},\n" +
            "  \"age\":37,\n" +
            "  \"children\": [\"Sara\",\"Alex\",\"Jack\"],\n" +
            "  \"fav.movie\": \"Deer Hunter\",\n" +
            "  \"friends\": [\n" +
            "    {\"first\": \"Dale\", \"last\": \"Murphy\", \"age\": 44},\n" +
            "    {\"first\": \"Roger\", \"last\": \"Craig\", \"age\": 68},\n" +
            "    {\"first\": \"Jane\", \"last\": \"Murphy\", \"age\": 47}\n" +
            "  ]\n" +
            "}";

    public static void main(String[] args) {
        new Nodaireacht(jsonString).get("name.first");
    }
}
