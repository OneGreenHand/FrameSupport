package com.ogh.support.widget;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StrikethroughSpan;

import org.xml.sax.Attributes;

import java.util.Stack;

public class HtmlTagHandler implements HtmlParser.TagHandler {
    /**
     * html 标签的开始下标
     */
    private Stack<Integer> startIndex = new Stack<>();

    /**
     * html的标签的属性值 value，如:<size value='16px'></size>
     */
    private Stack<String> propertyValue = new Stack<>();

    @Override
    public boolean handleTag(boolean opening, String tag, Editable output, Attributes attributes) {
        if (opening) {
            handlerStartTAG(tag, output, attributes);
        } else
            handlerEndTAG(tag, output, attributes);
        return handlerBYDefault(tag);
    }

    private void handlerStartTAG(String tag, Editable output, Attributes attributes) {
        if (tag.equalsIgnoreCase("font")) {
            handlerStartFONT(output, attributes);
        } else if (tag.equalsIgnoreCase("del"))
            handlerStartDEL(output);
    }

    private void handlerEndTAG(String tag, Editable output, Attributes attributes) {
        if (tag.equalsIgnoreCase("font")) {
            handlerEndFONT(output);
        } else if (tag.equalsIgnoreCase("del"))
            output.setSpan(new StrikethroughSpan(), startIndex.pop(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void handlerStartFONT(Editable output, Attributes attributes) {
        if (startIndex == null)
            startIndex = new Stack<>();
        startIndex.push(output.length());
        if (propertyValue == null)
            propertyValue = new Stack<>();
        propertyValue.push(getValue(attributes, "size"));
    }

    private String getValue(Attributes attributes, String name) {
        for (int i = 0, n = attributes.getLength(); i < n; i++) {
            if (name.equals(attributes.getLocalName(i)))
                return attributes.getValue(i);
        }
        return null;
    }

    private void handlerEndFONT(Editable output) {
        if (propertyValue != null && !propertyValue.isEmpty()) {
            try {
                String value = propertyValue.pop();
                int valueSize;
                if (value.contains("px")) {
                    valueSize = Integer.parseInt(value.substring(0, value.indexOf("px")));
                } else
                    valueSize = Integer.parseInt(value);
                output.setSpan(new AbsoluteSizeSpan(valueSize), startIndex.pop(), output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handlerStartDEL(Editable output) {
        if (startIndex == null)
            startIndex = new Stack<>();
        startIndex.push(output.length());
    }

    /**
     * 返回true表示不交给系统后续处理
     * false表示交给系统后续处理
     */
    private boolean handlerBYDefault(String tag) {
        return tag.equalsIgnoreCase("del");
    }
}

