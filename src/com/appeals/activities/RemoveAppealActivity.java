package com.appeals.activities;

import com.appeals.object.AppealStatus;
import com.appeals.object.Identifier;
import com.appeals.object.StudentAppeal;
import com.appeals.repositories.AppealsRepository;
import com.appeals.representation.AppealDeletionException;
import com.appeals.representation.AppealsRepresentation;
import com.appeals.representation.AppealsUri;
import com.appeals.representation.NoSuchAppealException;

public class RemoveAppealActivity {
    public AppealsRepresentation delete(AppealsUri orderUri) {
        // Discover the URI of the order that has been cancelled
        
        Identifier identifier = orderUri.getId();

        AppealsRepository appealRepository = AppealsRepository.current();

        if (appealRepository.appealNotPlaced(identifier)) {
            throw new NoSuchAppealException();
        }

        StudentAppeal appeal = appealRepository.get(identifier);

        // Can't delete a ready or preparing order
        if (appeal.getAppeal_status() == AppealStatus.ACCEPTED || appeal.getAppeal_status() == AppealStatus.REJECTED) {
            throw new AppealDeletionException();
        }

        if(appeal.getAppeal_status() == AppealStatus.INPROGRESS) { // An unpaid order is being cancelled 
        	appealRepository.remove(identifier);
        }

        return new AppealsRepresentation(appeal);
    }

}
