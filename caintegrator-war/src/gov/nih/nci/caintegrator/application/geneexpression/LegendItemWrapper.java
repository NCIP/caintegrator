/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;

import java.awt.Color;

import org.jfree.chart.LegendItem;

/**
 * Wraps a LegendItem to be able to print label and HTML hex color.
 */
public class LegendItemWrapper {
    private static final int HEX_0XF = 0xf;
    private final LegendItem legendItem;
    
    /**
     * Constructor.
     * @param legendItem object to wrap.
     */
    public LegendItemWrapper(LegendItem legendItem) {
        this.legendItem = legendItem;
    }
    
    /**
     * Label for legend.
     * @return label.
     */
    public String getLabel() {
        return legendItem.getLabel();
    }
    
    /**
     * Gets the HTML hex string for the color.
     * @return hex string for color.
     */
    public String getColor() {
        return c2hex((Color) legendItem.getFillPaint());
    }
    
    private String c2hex(Color c) {
        //http://rsb.info.nih.gov/ij/developer/source/index.html
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        
        int i = c.getRGB();
        char[] buf7 = new char[7];
        buf7[0] = '#';
        for (int pos = 6; pos >= 1; pos--) {
            buf7[pos] = hexDigits[i & HEX_0XF];
            i >>>= 4;
        }
        return new String(buf7);
    }

}
