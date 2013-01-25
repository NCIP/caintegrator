/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Helper class used to load the ExternalLinkList object.
 */
final class ExternalLinksLoader {
    
    private ExternalLinksLoader() { }
    
    static void loadLinks(ExternalLinkList externalLinkList) throws ValidationException, IOException {
        CSVReader reader = new CSVReader(new FileReader(externalLinkList.getFile()));
        String[] values;
        while ((values = reader.readNext()) != null) {
            if (values.length != 2) {
                throw new ValidationException("Invalid file format - Expect 2 columns but has " + values.length);
            }
            ExternalLink link = new ExternalLink();
            link.setName(values[0]);
            link.setUrl(values[1]);
            if (!link.getUrl().startsWith("http")) {
                throw new ValidationException("Invalid File Format - CSV must be of format 'link_name','link_url' "
                        + "and URL must start with 'http'");
            }
            externalLinkList.getExternalLinks().add(link);
        }
    }
}
