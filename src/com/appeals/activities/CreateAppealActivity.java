package com.appeals.activities;

import com.appeals.object.AppealStatus;
import com.appeals.object.Identifier;
import com.appeals.object.StudentAppeal;
import com.appeals.repositories.AppealsRepository;
import com.appeals.representation.AppealsRepresentation;
import com.appeals.representation.AppealsUri;
import com.appeals.representation.Link;
import com.appeals.representation.Representation;


public class CreateAppealActivity {
    public AppealsRepresentation create(StudentAppeal appeal, AppealsUri requestUri) {
        appeal.setAppeal_status(AppealStatus.INPROGRESS);
                
        Identifier identifier = AppealsRepository.current().store(appeal);
        
        AppealsUri appealUri = new AppealsUri(requestUri.getBaseUri() + "/appeals/" + identifier.toString());
        AppealsUri paymentUri = new AppealsUri(requestUri.getBaseUri() + "/followup/" + identifier.toString());
        return new AppealsRepresentation(appeal, 
                new Link(Representation.RELATIONS_URI + "delete", appealUri), 
                new Link(Representation.RELATIONS_URI + "followup", paymentUri), 
                new Link(Representation.RELATIONS_URI + "update", appealUri),
                new Link(Representation.SELF_REL_VALUE, appealUri));
    }
}
