package com.fis.fpt.uaadomain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchConditionDTO {
    private String owner;
    // The recipient id
    private String recipientId;
    // The recipient role
    private List<String> recipientRole;
    // The string key word for search
    private String keyWord;
    // The field for search from to (example : filed created Date)
    private String field;
    private String to;
    private String from;
    private String q;
    private Map<String, Object> terms = new HashMap<String, Object>();

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    private boolean advanced = false;

    private String orgIn;

    private String externalOrgIn;

    private List<String> listExternalOrgIn;
    private Long custId;

    private String contactId;

    /*
     * search in Acl
     */

    /*
     * search by refId
     */

    private String refId;

    private String resource;

    private String resourceId;


    private String personalID;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public List<String> getRecipientRole() {
        return recipientRole;
    }

    public void setRecipientRole(List<String> recipientRole) {
        this.recipientRole = recipientRole;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Map<String, Object> getTerms() {
        return terms;
    }

    public void setTerms(Map<String, Object> terms) {
        this.terms = terms;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public String getOrgIn() {
        return orgIn;
    }

    public void setOrgIn(String orgIn) {
        this.orgIn = orgIn;
    }

    public String getExternalOrgIn() {
        return externalOrgIn;
    }

    public void setExternalOrgIn(String externalOrgIn) {
        this.externalOrgIn = externalOrgIn;
    }

    public List<String> getListExternalOrgIn() {
        return listExternalOrgIn;
    }

    public void setListExternalOrgIn(List<String> listExternalOrgIn) {
        this.listExternalOrgIn = listExternalOrgIn;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }
}
