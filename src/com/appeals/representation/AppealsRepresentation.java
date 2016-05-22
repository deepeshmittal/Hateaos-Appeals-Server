package com.appeals.representation;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.appeals.object.StudentAppeal;
import com.appeals.object.AppealStatus;

@XmlRootElement(name = "appeal", namespace = Representation.CSE564APPEALS_NAMESPACE)
public class AppealsRepresentation extends Representation {
    
    @XmlElement(name = "classnumber", namespace = Representation.CSE564APPEALS_NAMESPACE)
    private String classnumber;
    @XmlElement(name = "studentID", namespace = Representation.CSE564APPEALS_NAMESPACE)
    private String studentID;
    @XmlElement(name = "workitem", namespace = Representation.CSE564APPEALS_NAMESPACE)
    private String workitem;
    @XmlElement(name = "grades", namespace = Representation.CSE564APPEALS_NAMESPACE)
    private String grades;
    @XmlElement(name = "appealcomment", namespace = Representation.CSE564APPEALS_NAMESPACE)
    private String appealcomment;
    @XmlElement(name = "followup", namespace = Representation.CSE564APPEALS_NAMESPACE)
    private String followup;
    @XmlElement(name = "instructorcomment", namespace = Representation.CSE564APPEALS_NAMESPACE)
    private String instructorcomment;
    @XmlElement(name = "status", namespace = Representation.CSE564APPEALS_NAMESPACE)
    private AppealStatus status;

    /**
     * For JAXB :-(
     */
    AppealsRepresentation() {
    }

    public static AppealsRepresentation fromXmlString(String xmlRepresentation) {
                
        AppealsRepresentation appealRepresentation = null;     
        try {
            JAXBContext context = JAXBContext.newInstance(AppealsRepresentation.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            appealRepresentation = (AppealsRepresentation) unmarshaller.unmarshal(new ByteArrayInputStream(xmlRepresentation.getBytes()));
        } catch (Exception e) {
            throw new InvalidAppealException(e);
        }
        
        return appealRepresentation;
    }
    
    public static AppealsRepresentation createResponseOrderRepresentation(StudentAppeal appeal, AppealsUri appealUri) {
        
        AppealsRepresentation appealRepresentation = null; 
        
        AppealsUri followupUri = new AppealsUri(appealUri.getBaseUri() + "/followup/" + appealUri.getId().toString());
        
        if(appeal.getAppeal_status() == AppealStatus.INPROGRESS) {
        	appealRepresentation = new AppealsRepresentation(appeal, 
                    new Link(RELATIONS_URI + "delete", appealUri), 
                    new Link(RELATIONS_URI + "followup", followupUri), 
                    new Link(RELATIONS_URI + "update", appealUri),
                    new Link(Representation.SELF_REL_VALUE, appealUri));
        } else if(appeal.getAppeal_status() == AppealStatus.REJECTED || appeal.getAppeal_status() == AppealStatus.ACCEPTED) {
            appealRepresentation = new AppealsRepresentation(appeal, new Link(Representation.SELF_REL_VALUE, appealUri));
        } else {
            throw new RuntimeException("Unknown Order Status");
        }
        return appealRepresentation;
    }

    public AppealsRepresentation(StudentAppeal appeal, Link... links) {
        
        try {
        	this.classnumber = appeal.getClassNumber();
    		this.studentID = appeal.getStudentid();
    		this.workitem = appeal.getWorkItem();
    		this.grades = appeal.getStudent_grades();
    		this.appealcomment = appeal.getAppealComment();
    		this.followup = appeal.getStudent_followup();
    		this.instructorcomment = appeal.getInstructorComment();
            this.status = appeal.getAppeal_status();
            this.links = java.util.Arrays.asList(links);
        } catch (Exception ex) {
            throw new InvalidAppealException(ex);
        }
        
    }

    public String toString() {
        try {
            JAXBContext context = JAXBContext.newInstance(AppealsRepresentation.class);
            Marshaller marshaller = context.createMarshaller();

            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(this, stringWriter);

            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StudentAppeal getAppeal() {
    	StudentAppeal appeal = new StudentAppeal();
    	appeal.setClassNumber(classnumber);
    	appeal.setStudentid(studentID);
    	appeal.setWorkItem(workitem);
    	appeal.setStudent_grades(grades);
    	appeal.setAppealComment(appealcomment);
    	appeal.setAppeal_status(status);
    	appeal.setStudent_followup(followup);
    	appeal.setInstructorComment(instructorcomment);
        return appeal;
    }

    public Link getDeleteLink() {
        return getLinkByName(RELATIONS_URI + "delete");
    }

    public Link getFollowUpLink() {
        return getLinkByName(RELATIONS_URI + "followup");
    }

    public Link getUpdateLink() {
        return getLinkByName(RELATIONS_URI + "update");
    }

    public Link getSelfLink() {
        return getLinkByName("self"); 
    }
    
    public AppealStatus getStatus() {
        return status;
    }
}
