/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

/**
 * 
 */public class SelectQueryTabActionTest {
     
     @Test
     public void testSelectQueryTabAction() throws Exception {
         SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
         ActionContext.getContext().setSession(new HashMap<String, Object>());

         SelectQueryTabAction action = new SelectQueryTabAction();
         action.setSelectedAction("selectSortingTab");
         assertEquals("selectSortingTab", action.getSelectedAction());
         assertEquals("selectSortingTab", action.execute());
         action.setSelectedAction("selectSortingTab,Dummy");
         assertEquals("selectSortingTab", action.getSelectedAction());
         assertEquals(20, action.getPageSize());
         action.setQueryId(1L);
         assertEquals(1, action.getQueryId().longValue());
     }

 }
