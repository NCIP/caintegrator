/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.ImageContentTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyLogo;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.opensymphony.xwork2.mock.MockActionInvocation;

public class StudyLogoResultTest extends AbstractSessionBasedTest {

    @Test
    public void testHandleRequest() {
        setUp();
        StudyLogoResult studyLogoResult = new StudyLogoResult();
        StudyLogo studyLogo = new StudyLogo();
        studyLogo.setFileType(ImageContentTypeEnum.JPEG.getValue());
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setResponse(response);
        ServletActionContext.setRequest(request);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setStudyLogo(studyLogo);
        // Catching these exceptions, because the actual logo files don't exist right now on the filesystem.
        try {
            studyLogoResult.execute(new MockActionInvocation());
            fail();
        } catch (IOException e) {
        }
        assertEquals("image/gif", response.getContentType());
        
        studyLogo.setPath(TestDataFiles.VALID_FILE_RESOURCE_PATH);
        SessionHelper.getInstance().getDisplayableUserWorkspace().setStudyLogo(studyLogo);
        try {
            studyLogoResult.execute(new MockActionInvocation());
            fail();
        } catch (IOException e) {
        }
        assertEquals(ImageContentTypeEnum.JPEG.getValue(), response.getContentType());
    }

}
