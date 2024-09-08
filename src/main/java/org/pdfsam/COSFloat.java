package org.pdfsam;
/*
 * This file is part of the PDF Black project
 * Created on 06/09/24
 * Copyright 2024 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * You are not permitted to distribute it in any form unless explicit
 * consent is given by Sober Lemur S.r.l.
 * You are not permitted to modify it.
 *
 * PDF Black is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

import org.sejda.sambox.cos.COSVisitor;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

import static org.sejda.commons.util.RequireUtils.requireIOCondition;

/**
 * @author Andrea Vacondio
 */
public class COSFloat {
    private float value;
    private static final Pattern DOTS = Pattern.compile("\\.");
    private static final Pattern EXP_END = Pattern.compile("[e|E]$");
    private static final Pattern NUM1 = Pattern.compile("^(-)([-|+]+)\\d+\\.\\d+");
    private static final Pattern NUM2 = Pattern.compile("^(-)([\\-|+]+)");
    private static final Pattern NUM3 = Pattern.compile("^0\\.0*-\\d+");
    private static final Pattern ZERO = Pattern.compile("^0-(\\.|\\d+)*");
    private static final Pattern MINUS = Pattern.compile("-");


    /**
     * @param aFloat The primitive float object that this object wraps.
     * @throws IOException If aFloat is not a float.
     */
    public COSFloat(String aFloat) throws IOException
    {
        try
        {
            // no pre-processing! speed optimized for the vanilla scenario where we parse good floats
            // if we encounter a problem, then we start handling edge cases, not before
            value = Float.parseFloat(aFloat);
        }
        catch (NumberFormatException e)
        {
            try
            {
                int dot = aFloat.indexOf('.');
                if (dot != aFloat.lastIndexOf('.'))
                {
                    // 415.75.795 we replace additional dots with 0
                    aFloat = aFloat.substring(0, dot + 1)
                            + DOTS.matcher(aFloat.substring(dot + 1)).replaceAll("0");

                }
                aFloat = EXP_END.matcher(aFloat).replaceAll("");
                value = Float.parseFloat(aFloat);
            }
            catch (NumberFormatException nfex)
            {
                try
                {
                    if (NUM1.matcher(aFloat).matches())
                    {
                        // PDFBOX-3589 --242.0
                        value = Float.parseFloat(NUM2.matcher(aFloat).replaceFirst("-"));
                    }
                    else if (ZERO.matcher(aFloat).matches())
                    {
                        // SAMBox 75
                        value = 0f;
                    }
                    else
                    {
                        // PDFBOX-2990 has 0.00000-33917698
                        // PDFBOX-3369 has 0.00-35095424
                        // PDFBOX-3500 has 0.-262
                        requireIOCondition(NUM3.matcher(aFloat).matches(),
                                           "Expected floating point number but found '" + aFloat + "'");
                        value = Float.parseFloat("-" + MINUS.matcher(aFloat).replaceFirst(""));
                    }
                }
                catch (NumberFormatException e2)
                {
                    throw new IOException(
                            "Error expected floating point number actual='" + aFloat + "'", e2);
                }
            }
            value = coerce(value);
        }
    }

    private float coerce(float floatValue)
    {
        if (floatValue == Float.POSITIVE_INFINITY)
        {
            return Float.MAX_VALUE;
        }
        if (floatValue == Float.NEGATIVE_INFINITY)
        {
            return -Float.MAX_VALUE;
        }
        if (Math.abs(floatValue) < Float.MIN_NORMAL)
        {
            // values smaller than the smallest possible float value are converted to 0
            // see PDF spec, chapter 2 of Appendix C Implementation Limits
            return 0f;
        }
        return floatValue;
    }


}
