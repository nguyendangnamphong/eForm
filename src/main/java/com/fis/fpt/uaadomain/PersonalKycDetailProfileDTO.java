package com.fis.fpt.uaadomain;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class PersonalKycDetailProfileDTO implements Serializable {

    private Long id;

    @Size(max = 1000)
    private String agreementUUID;

    @Size(max = 1000)
    private String personalName;

    @Size(max = 1000)
    private String organization;

    private String organizationUnit;

    private String title;

    private String email;

    private String telephoneNumber;

    @Size(max = 1000)
    private String location;

    @Size(max = 1000)
    private String stateOrProvince;

    @Size(max = 1000)
    private String country;

    private String personalID;

    private String passportID;

    private String taxID;

    private String budgetID;

    @Lob
    private byte[] applicationForm;

    private String applicationFormContentType;
    @Lob
    private byte[] requestForm;

    private String requestFormContentType;

    @Lob
    private byte[] authorizeLetter;

    private String authorizeLetterContentType;
    @Lob
    private byte[] photoIDCard;

    private String photoIDCardContentType;
    @Lob
    private byte[] photoActivityDeclaration;

    private String photoActivityDeclarationContentType;
    @Lob
    private byte[] photoAuthorizeDelegate;

    private String photoAuthorizeDelegateContentType;
    private String refId;

    private String staffId;

    private Long custId;

    @Size(max = 1000)
    private String orgIn;

    @Size(max = 1000)
    private String externalOrgIn;

    private String messageType;

    private ZonedDateTime fromDate;

    private ZonedDateTime toDate;

    private String createdBy;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModifiedDate;

    private String lastModifiedBy;

    private String statusCode;

    private String resourceType;

    private String label;

    private String password;

    private Boolean active;

    private String algorithm;

    private String type;

    @Lob
    private byte[] photoFrontSideIDCard;

    private String photoFrontSideIDCardContentType;
    @Lob
    private byte[] photoBackSideIDCard;

    private String photoBackSideIDCardContentType;

    private Map<String, Object> ekycContent;

    private String provideAddress;

    private String provideDate;

    @Size(max = 2000)
    private String address;

    @Lob
    private byte[] sampleDocument;

    private String sampleDocumentContentType;
    private String statusConfirm;

    @Size(max = 2000)
    private String commune;

    private List<Document> documents;

    private Map<String, Object> attrs;

    private List<String> hashTag;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

    public String getPersonalName() {
        return personalName;
    }

    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getBudgetID() {
        return budgetID;
    }

    public void setBudgetID(String budgetID) {
        this.budgetID = budgetID;
    }

    public byte[] getApplicationForm() {
        return applicationForm;
    }

    public void setApplicationForm(byte[] applicationForm) {
        this.applicationForm = applicationForm;
    }

    public String getApplicationFormContentType() {
        return applicationFormContentType;
    }

    public void setApplicationFormContentType(String applicationFormContentType) {
        this.applicationFormContentType = applicationFormContentType;
    }

    public byte[] getRequestForm() {
        return requestForm;
    }

    public void setRequestForm(byte[] requestForm) {
        this.requestForm = requestForm;
    }

    public String getRequestFormContentType() {
        return requestFormContentType;
    }

    public void setRequestFormContentType(String requestFormContentType) {
        this.requestFormContentType = requestFormContentType;
    }

    public byte[] getAuthorizeLetter() {
        return authorizeLetter;
    }

    public void setAuthorizeLetter(byte[] authorizeLetter) {
        this.authorizeLetter = authorizeLetter;
    }

    public String getAuthorizeLetterContentType() {
        return authorizeLetterContentType;
    }

    public void setAuthorizeLetterContentType(String authorizeLetterContentType) {
        this.authorizeLetterContentType = authorizeLetterContentType;
    }

    public byte[] getPhotoActivityDeclaration() {
        return photoActivityDeclaration;
    }

    public void setPhotoActivityDeclaration(byte[] photoActivityDeclaration) {
        this.photoActivityDeclaration = photoActivityDeclaration;
    }

    public String getPhotoActivityDeclarationContentType() {
        return photoActivityDeclarationContentType;
    }

    public void setPhotoActivityDeclarationContentType(String photoActivityDeclarationContentType) {
        this.photoActivityDeclarationContentType = photoActivityDeclarationContentType;
    }

    public byte[] getPhotoAuthorizeDelegate() {
        return photoAuthorizeDelegate;
    }

    public void setPhotoAuthorizeDelegate(byte[] photoAuthorizeDelegate) {
        this.photoAuthorizeDelegate = photoAuthorizeDelegate;
    }

    public String getPhotoAuthorizeDelegateContentType() {
        return photoAuthorizeDelegateContentType;
    }

    public void setPhotoAuthorizeDelegateContentType(String photoAuthorizeDelegateContentType) {
        this.photoAuthorizeDelegateContentType = photoAuthorizeDelegateContentType;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
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

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public ZonedDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(ZonedDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public ZonedDateTime getToDate() {
        return toDate;
    }

    public void setToDate(ZonedDateTime toDate) {
        this.toDate = toDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getPhotoIDCard() {
        return photoIDCard;
    }

    public void setPhotoIDCard(byte[] photoIDCard) {
        this.photoIDCard = photoIDCard;
    }

    public String getPhotoIDCardContentType() {
        return photoIDCardContentType;
    }

    public void setPhotoIDCardContentType(String photoIDCardContentType) {
        this.photoIDCardContentType = photoIDCardContentType;
    }

    public byte[] getPhotoFrontSideIDCard() {
        return photoFrontSideIDCard;
    }

    public void setPhotoFrontSideIDCard(byte[] photoFrontSideIDCard) {
        this.photoFrontSideIDCard = photoFrontSideIDCard;
    }

    public String getPhotoFrontSideIDCardContentType() {
        return photoFrontSideIDCardContentType;
    }

    public void setPhotoFrontSideIDCardContentType(String photoFrontSideIDCardContentType) {
        this.photoFrontSideIDCardContentType = photoFrontSideIDCardContentType;
    }

    public byte[] getPhotoBackSideIDCard() {
        return photoBackSideIDCard;
    }

    public void setPhotoBackSideIDCard(byte[] photoBackSideIDCard) {
        this.photoBackSideIDCard = photoBackSideIDCard;
    }

    public String getPhotoBackSideIDCardContentType() {
        return photoBackSideIDCardContentType;
    }

    public void setPhotoBackSideIDCardContentType(String photoBackSideIDCardContentType) {
        this.photoBackSideIDCardContentType = photoBackSideIDCardContentType;
    }

    public Map<String, Object> getEkycContent() {
        return ekycContent;
    }

    public void setEkycContent(Map<String, Object> ekycContent) {
        this.ekycContent = ekycContent;
    }

    public String getProvideAddress() {
        return provideAddress;
    }

    public void setProvideAddress(String provideAddress) {
        this.provideAddress = provideAddress;
    }

    public String getProvideDate() {
        return provideDate;
    }

    public void setProvideDate(String provideDate) {
        this.provideDate = provideDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getSampleDocument() {
        return sampleDocument;
    }

    public void setSampleDocument(byte[] sampleDocument) {
        this.sampleDocument = sampleDocument;
    }

    public String getSampleDocumentContentType() {
        return sampleDocumentContentType;
    }

    public void setSampleDocumentContentType(String sampleDocumentContentType) {
        this.sampleDocumentContentType = sampleDocumentContentType;
    }

    public String getStatusConfirm() {
        return statusConfirm;
    }

    public void setStatusConfirm(String statusConfirm) {
        this.statusConfirm = statusConfirm;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public List<String> getHashTag() {
        return hashTag;
    }

    public void setHashTag(List<String> hashTag) {
        this.hashTag = hashTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalKycDetailProfileDTO)) {
            return false;
        }

        return id != null && id.equals(((PersonalKycDetailProfileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalKycDetailProfileDTO{" + "id=" + getId() + ", agreementUuid='" + getAgreementUUID() + "'"
            + ", personalName='" + getPersonalName() + "'" + ", organization='" + getOrganization() + "'"
            + ", organizationUnit='" + getOrganizationUnit() + "'" + ", title='" + getTitle() + "'" + ", email='"
            + getEmail() + "'" + ", telephoneNumber='" + getTelephoneNumber() + "'" + ", location='" + getLocation()
            + "'" + ", stateOrProvince='" + getStateOrProvince() + "'" + ", country='" + getCountry() + "'"
            + ", personalId='" + getPersonalID() + "'" + ", passportId='" + getPassportID() + "'" + ", taxId='"
            + getTaxID() + "'" + ", budgetId='" + getBudgetID() + "'" + ", applicationForm='" + getApplicationForm()
            + "'" + ", requestForm='" + getRequestForm() + "'" + ", authorizeLetter='" + getAuthorizeLetter() + "'"
            + ", photoIdCard='" + getPhotoIDCard() + "'" + ", photoActivityDeclaration='"
            + getPhotoActivityDeclaration() + "'" + ", photoAuthorizeDelegate='" + getPhotoAuthorizeDelegate() + "'"
            + ", refId='" + getRefId() + "'" + ", staffId='" + getStaffId() + "'" + ", custId=" + getCustId()
            + ", orgIn='" + getOrgIn() + "'" + ", externalOrgIn='" + getExternalOrgIn() + "'" + ", messageType='"
            + getMessageType() + "'" + ", fromDate='" + getFromDate() + "'" + ", toDate='" + getToDate() + "'"
            + ", createdBy='" + getCreatedBy() + "'" + ", createdDate='" + getCreatedDate() + "'"
            + ", lastModifiedDate='" + getLastModifiedDate() + "'" + ", lastModifiedBy='" + getLastModifiedBy()
            + "'" + ", statusCode='" + getStatusCode() + "'" + ", resourceType='" + getResourceType() + "'"
            + ", label='" + getLabel() + "'" + ", password='" + getPassword() + "'" + ", active='" + isActive()
            + "'" + ", algorithm='" + getAlgorithm() + "'" + ", type='" + getType() + "'"
            + ", photoFrontSideIdCard='" + getPhotoFrontSideIDCard() + "'" + ", photoBackSideIdCard='"
            + getPhotoBackSideIDCard() + "'" + ", ekycContent='" + getEkycContent() + "'" + ", provideAddress='"
            + getProvideAddress() + "'" + ", provideDate='" + getProvideDate() + "'" + ", address='" + getAddress()
            + "'" + ", sampleDocument='" + getSampleDocument() + "'" + ", statusConfirm='" + getStatusConfirm()
            + "'" + ", commune='" + getCommune() + "'" + ", documents='" + getDocuments() + "'" + ", attrs='"
            + getAttrs() + "'" + ", hashTag='" + getHashTag() + "'" + "}";
    }
}
