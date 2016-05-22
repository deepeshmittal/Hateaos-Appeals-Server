package com.appeals.activities;

import com.appeals.object.Identifier;
import com.appeals.object.StudentAppeal;
import com.appeals.repositories.AppealsRepository;
import com.appeals.representation.AppealsRepresentation;
import com.appeals.representation.AppealsUri;
import com.appeals.representation.NoSuchAppealException;

public class GetAppealActivity {
    public AppealsRepresentation retrieveByUri(AppealsUri appealUri) {
        Identifier identifier  = appealUri.getId();
        
        StudentAppeal appeal = AppealsRepository.current().get(identifier);
        
        if(appeal == null) {
            throw new NoSuchAppealException();
        }
        
        return AppealsRepresentation.createResponseOrderRepresentation(appeal, appealUri);
    }
}
