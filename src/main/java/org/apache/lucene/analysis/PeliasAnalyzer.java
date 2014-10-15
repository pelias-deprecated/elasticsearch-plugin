package org.apache.lucene.analysis;

import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.util.CharacterUtils;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.util.Version;
import org.elasticsearch.PeliasPlugin.Tools;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.IOUtils;

import javax.swing.text.html.HTMLDocument;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class PeliasAnalyzer extends Analyzer {
    private final Version version;

    public PeliasAnalyzer(Version version) {
        this.version = version;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName,
                                                     Reader reader) {
        Tokenizer source = new WhitespaceTokenizer(version, reader);
        TokenStream filter = new LowerCaseFilter(version, source);
        filter = new ASCIIFoldingFilter(filter);
        //filter = new PatternReplaceFilter(filter, Pattern.compile("[&]"), " and ", true);
        try {
            JSONObject mappings = getMappings();
            SynonymMap.Builder builder = addTerms(mappings);
            SynonymMap synonyms = builder.build();
            filter = new SynonymFilter(filter, synonyms, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        filter = new WordDelimiterFilter(version, filter, 0, null);

        return new TokenStreamComponents(source, filter);
    }
}
