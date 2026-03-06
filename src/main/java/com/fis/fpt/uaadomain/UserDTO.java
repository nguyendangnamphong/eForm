package com.fis.fpt.uaadomain;

import com.fis.fpt.config.Constants;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;
    @Size(max = 2)
    private String gender;

    @Size(max = 255)
    private String address;
    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    private String phone;

    private Instant dob;

    private byte[] avatar ;

    private byte[] signature;

    private Long custId;

    // 2 bien phuc vu hien thi
    private String orgName;
    private String custType;
    private String parentOrgName;
    private String taxCodeOrg;
    private String acNameOrg;
    private String orgCode;
    private Organization organization;
    private List<Organization> subOrganizations;
    // 2 bien phuc vu hien thi

    // for save log
    private String activityLogName;
    private String groupId;
    private Long userId;

    public String getActivityLogName() {
        return activityLogName;
    }

    public void setActivityLogName(String activityLogName) {
        this.activityLogName = activityLogName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Size(max = 256)
    private String orgIn;

    @Size(max = 5)
    private String dbSuffix;

    private Boolean blocked;

    @Size(max = 256)
    private String folderPath;

    @Size(max = 256)
    private String folderId;

    private Long orgId;

    private FieldDesign digitalSignature;

    public FieldDesign getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(FieldDesign digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public String getFolderId() {
        return folderId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public String getTaxCodeOrg() {
        return taxCodeOrg;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setTaxCodeOrg(String taxCodeOrg) {
        this.taxCodeOrg = taxCodeOrg;
    }

    public String getAcNameOrg() {
        return acNameOrg;
    }

    public void setAcNameOrg(String acNameOrg) {
        this.acNameOrg = acNameOrg;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet());
        this.phone = user.getPhone();
        this.dob = user.getDob();
        this.avatar = user.getAvatar();
        this.signature = user.getSignature();
        this.custId = user.getCustId();
        this.orgIn = user.getOrgIn();
        this.dbSuffix = user.getDbSuffix();
        this.blocked = user.getBlocked();
        this.folderPath = user.getFolderPath();
        this.folderId = user.getFolderId();
        this.address = user.getAddress();
        this.gender = user.getGender();
        this.digitalSignature = user.getDigitalSignature();
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

    public String getDbSuffix() {
        return dbSuffix;
    }

    public void setDbSuffix(String dbSuffix) {
        this.dbSuffix = dbSuffix;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getDob() {
        return dob;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "UserDTO [id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName
            + ", email=" + email + ", imageUrl=" + imageUrl + ", activated=" + activated + ", langKey=" + langKey
            + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy
            + ", lastModifiedDate=" + lastModifiedDate + ", authorities=" + authorities + ", phone=" + phone
            + ", dob=" + dob + ", avatar=" + Arrays.toString(avatar) + ", signature=" + Arrays.toString(signature)
            + ", custId=" + custId + ", orgIn=" + orgIn + ", dbSuffix=" + dbSuffix + ", blocked=" + blocked
            + ", folderPath=" + folderPath + ", folderId=" + folderId + "]";
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Organization> getSubOrganizations() {
        return subOrganizations;
    }

    public void setSubOrganizations(List<Organization> subOrganizations) {
        this.subOrganizations = subOrganizations;
    }
}
