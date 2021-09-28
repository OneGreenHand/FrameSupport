package com.ogh.support.widget;

import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.util.ArrayDeque;

public class HtmlParser implements Html.TagHandler, ContentHandler {

    private TagHandler handler;
    private ContentHandler wrapped;
    private Editable text;
    private final ArrayDeque<Boolean> tagStatus = new ArrayDeque<>();

    public HtmlParser() {
    }

    private HtmlParser(TagHandler handler) {
        this.handler = handler;
    }

    //This approach has the advantage that it allows to disable processing of some tags while using default processing for others,
    // e.g. you can make sure that ImageSpan objects are not created:
    public interface TagHandler {
        // return true here to indicate that this tag was handled and
        // should not be processed further
        boolean handleTag(boolean opening, String tag, Editable output, Attributes attributes);
    }

    public Spanned buildSpannedText(String html) {
        // add a tag at the start that is not handled by default,
        // allowing custom tag handler to replace xmlReader contentHandler
        if (TextUtils.isEmpty(html))
            return Html.fromHtml("");
        else {
            Spanned spanned;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                spanned = Html.fromHtml("<inject/>" + html, Html.FROM_HTML_MODE_COMPACT, null, new HtmlParser(new HtmlTagHandler()));
            else
                spanned = Html.fromHtml("<inject/>" + html, null, new HtmlParser(new HtmlTagHandler()));
            return spanned;
        }
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (wrapped == null) {
            // record result object
            text = output;
            // record current content handler
            wrapped = xmlReader.getContentHandler();
            // replace content handler with our own that forwards to calls to original when needed
            xmlReader.setContentHandler(this);
            // handle endElement() callback for <inject/> tag
            tagStatus.addLast(Boolean.FALSE);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        boolean isHandled = handler.handleTag(true, localName, text, attributes);
        tagStatus.addLast(isHandled);
        if (!isHandled)
            wrapped.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (!tagStatus.removeLast())
            wrapped.endElement(uri, localName, qName);
        handler.handleTag(false, localName, text, null);
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        wrapped.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        wrapped.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        wrapped.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        wrapped.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        wrapped.endPrefixMapping(prefix);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        wrapped.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        wrapped.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        wrapped.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        wrapped.skippedEntity(name);
    }

}
