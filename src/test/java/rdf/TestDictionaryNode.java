package rdf;

import junit.framework.TestCase;


public class TestDictionaryNode extends TestCase {

    public void testGetDictionaryPath() {
        DictionaryNode dictionaryNode = DictionaryNode.getInstance();
        dictionaryNode.setDictionaryPath("test_path");
        assertEquals("test_path", dictionaryNode.getDictionaryPath());
    }
}
