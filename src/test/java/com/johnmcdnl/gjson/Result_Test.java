package com.johnmcdnl.gjson;

import org.junit.Assert;
import org.junit.Test;

public class Result_Test {

    @Test
    public void getString() {
        final String json = "{\"key\": \"strValue\"}";
        GJson g = new GJson(json);
        Assert.assertEquals("strValue", g.string("key"));
    }

    @Test
    public void getObject() {
        final String json = "{\"object\":{\"key1\":\"val1\",\"key2\":2}}";
        GJson g = new GJson(json);
        Assert.assertEquals("{\"key1\":\"val1\",\"key2\":2}", g.string("object"));
        Assert.assertEquals("val1", g.string("object.key1"));
        Assert.assertEquals("2", g.string("object.key2"));
    }

    @Test
    public void getArray() {
        final String json = "{\"key\":[\"a\",\"b\",2,true]}";
        GJson g = new GJson(json);
        Assert.assertEquals("[\"a\",\"b\",2,true]", g.string("key"));
        Assert.assertEquals("a", g.string("key.0"));
        Assert.assertEquals("b", g.string("key.1"));
        Assert.assertEquals("2", g.string("key.2"));
        Assert.assertEquals("true", g.string("key.3"));
    }

    @Test
    public void getBoolean() {
        final String json = "{\"key1\":false,\"key2\":true,\"key3\":0,\"key4\":1}";
        GJson g = new GJson(json);
        Assert.assertEquals("false", g.string("key1"));
        Assert.assertEquals("true", g.string("key2"));
        Assert.assertEquals("0", g.string("key3"));
        Assert.assertEquals("1", g.string("key4"));
        Assert.assertFalse(g.asBoolean("key1"));
        Assert.assertTrue(g.asBoolean("key2"));
        Assert.assertFalse(g.asBoolean("key3"));
        Assert.assertFalse(g.asBoolean("key4"));
    }

    @Test
    public void getNumber() {
        final String json = "{\"key1\":1,\"key2\":0,\"key3\":1.0,\"key4\":-1,\"key5\":234,\"key6\":-10000.3,\"key7\":1222223445,\"key8\":122223.445}";
        GJson g = new GJson(json);
        Assert.assertEquals(1, g.number("key1"), 0);
        Assert.assertEquals(0, g.number("key2"), 0);
        Assert.assertEquals(1, g.number("key3"), 0);
        Assert.assertEquals(-1, g.number("key4"), 0);
        Assert.assertEquals(234, g.number("key5"), 0);
        Assert.assertEquals(-10000.3, g.number("key6"), 0);
        Assert.assertEquals(1222223445, g.number("key7"), 0);
        Assert.assertEquals(122223.445, g.number("key8"), 0);
    }

    @Test(expected = RuntimeException.class)
    public void getNull() {
        final String json = "{\"key1\":null,\"key2\":\"null\"}";
        GJson g = new GJson(json);

        Assert.assertNull(g.asNull("key1"));
        Assert.assertNotNull(g.asNull("key2"));

        Assert.assertTrue(g.get("key1").isNull());
        Assert.assertFalse(g.get("key2").isNull());

        Assert.assertEquals("null", g.string("key1"));
        Assert.assertEquals("null", g.string("key2"));
    }
}
