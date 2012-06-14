package com.bla.laa;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class TextRenderListener
        implements RenderListener
{

    private StringBuffer sb = new StringBuffer();

    public void beginTextBlock()
    {
    }

    public void endTextBlock()
    {
    }

    public void renderImage(ImageRenderInfo renderInfo)
    {
    }

    public void renderText(TextRenderInfo renderInfo)
    {
        sb.append(renderInfo.getText().trim());
    }

    public String getSb()
    {
        return sb.toString();
    }
}
