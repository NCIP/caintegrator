/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import java.io.File;

public class AnnotationFileStub extends AnnotationFile {
    
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("deprecation") // for the stub.
    public AnnotationFileStub() {
        
    }
    
    @Override
    void positionAtData() throws ValidationException {
        
    }
    
    @Override
    boolean hasNextDataLine() throws ValidationException {
        return false;
    }
    
    @Override
    public File getFile() {
        return new FileStub();
    }
    
    private class FileStub extends File {
        
        private static final long serialVersionUID = 1L;

        public FileStub() {
            super("");
        }
        
        @Override
        public String getName() {
            return "file";
        }
    }
}
