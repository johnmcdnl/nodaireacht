package com.johnmcdnl.nodaireacht;

import org.junit.Assert;
import org.junit.Test;

public class Result_Test {

    @Test
    public void getString() {
        final String json = "{\"key\": \"strValue\"}";
        Nodaireacht n = new Nodaireacht(json);
        Assert.assertEquals("strValue", n.string("key"));
    }

    @Test
    public void getObject() {
        final String json = "{\"object\":{\"key1\":\"val1\",\"key2\":2}}";
        Nodaireacht n = new Nodaireacht(json);
        Assert.assertEquals("{\"key1\":\"val1\",\"key2\":2}", n.string("object"));
        Assert.assertEquals("val1", n.string("object.key1"));
        Assert.assertEquals("2", n.string("object.key2"));
    }

    @Test
    public void getArray() {
        final String json = "{\"key\":[\"a\",\"b\",2,true]}";
        Nodaireacht n = new Nodaireacht(json);
        Assert.assertEquals("[\"a\",\"b\",2,true]", n.string("key"));
        Assert.assertEquals("a", n.string("key.0"));
        Assert.assertEquals("b", n.string("key.1"));
        Assert.assertEquals("2", n.string("key.2"));
        Assert.assertEquals("true", n.string("key.3"));
    }

    @Test
    public void getBoolean() {
        final String json = "{\"key1\":false,\"key2\":true,\"key3\":0,\"key4\":1}";
        Nodaireacht n = new Nodaireacht(json);
        Assert.assertEquals("false", n.string("key1"));
        Assert.assertEquals("true", n.string("key2"));
        Assert.assertEquals("0", n.string("key3"));
        Assert.assertEquals("1", n.string("key4"));
        Assert.assertFalse(n.asBoolean("key1"));
        Assert.assertTrue(n.asBoolean("key2"));
        Assert.assertFalse(n.asBoolean("key3"));
        Assert.assertFalse(n.asBoolean("key4"));
    }

    @Test
    public void getNumber() {
        final String json = "{\"key1\":1,\"key2\":0,\"key3\":1.0,\"key4\":-1,\"key5\":234,\"key6\":-10000.3,\"key7\":1222223445,\"key8\":122223.445}";
        Nodaireacht n = new Nodaireacht(json);
        Assert.assertEquals(1, n.number("key1"), 0);
        Assert.assertEquals(0, n.number("key2"), 0);
        Assert.assertEquals(1, n.number("key3"), 0);
        Assert.assertEquals(-1, n.number("key4"), 0);
        Assert.assertEquals(234, n.number("key5"), 0);
        Assert.assertEquals(-10000.3, n.number("key6"), 0);
        Assert.assertEquals(1222223445, n.number("key7"), 0);
        Assert.assertEquals(122223.445, n.number("key8"), 0);
    }

    @Test(expected = RuntimeException.class)
    public void getNull() {
        final String json = "{\"key1\":null,\"key2\":\"null\"}";
        Nodaireacht n = new Nodaireacht(json);

        Assert.assertNull(n.asNull("key1"));
        Assert.assertNotNull(n.asNull("key2"));

        Assert.assertTrue(n.get("key1").isNull());
        Assert.assertFalse(n.get("key2").isNull());

        Assert.assertEquals("null", n.string("key1"));
        Assert.assertEquals("null", n.string("key2"));
    }
}
