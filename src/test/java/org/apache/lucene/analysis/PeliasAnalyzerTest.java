package org.apache.lucene.analysis;

import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.Version;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

public class PeliasAnalyzerTest {

    PeliasAnalyzer analyzer = new PeliasAnalyzer(Version.LUCENE_4_9);

    @Test
    public void testAnalyzerCreation() {
        analyzer.createComponents("", new StringReader("BALLAS st"));
    }
    @Test
    public void testGetMappings() throws Exception {
        assertNotNull(analyzer.getMappings());
    }

    @Test
    public void testTermAdd() throws Exception {
        JSONObject mappings = analyzer.getMappings();
        assertNotNull(analyzer.addTerms(mappings));
    }
}