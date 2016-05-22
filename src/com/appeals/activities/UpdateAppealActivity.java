package com.appeals.activities;

import com.appeals.object.AppealStatus;
import com.appeals.object.Identifier;
import com.appeals.object.StudentAppeal;
import com.appeals.repositories.AppealsRepository;
import com.appeals.representation.AppealsRepresentation;
import com.appeals.representation.AppealsUri;
import com.appeals.representation.NoSuchAppealException;
import com.appeals.representation.UpdateException;


public class UpdateAppealActivity {
    public AppealsRepresentation update(StudentAppeal appeal, AppealsUri appealUri) {
        Identifier appealIdentifier = appealUri.getId();

        AppealsRepository repository = AppealsRepository.current();
        if (AppealsRepository.current().appealNotPlaced(appealIdentifier)) { // Defensive check to see if we have the order
            throw new NoSuchAppealException();
        }

        if (!appealCanBeChanged(appealIdentifier)) {
            throw new UpdateException();
        }

        repository.store(appealIdentifier, appeal);
        

        return AppealsRepresentation.createResponseOrderRepresentation(appeal, appealUri); 
    }
    
    private boolean appealCanBeChanged(Identifier identifier) {
        return AppealsRepository.current().get(identifier).getAppeal_status() == AppealStatus.INPROGRESS;
    }
}
