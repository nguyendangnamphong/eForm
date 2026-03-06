package com.fis.fpt.uaadomain;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class OrganizationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 256)
    private String name;

    @NotNull
    @Size(max = 256)
    private String acName;

    @NotNull
    @Max(value = 999999999)
    private Integer custId;

    @NotNull
    @Size(max = 50)
    private String folderName;

    @NotNull
    @Size(max = 256)
    private String folderPath;

    @Size(max = 30)
    private String orgCode;

    @Max(value = 999999999)
    private Long parentId;

    private String type;

    @NotNull
    private String orgIn;

    private Long idOwner;

    @Size(max = 60)
    private String folderId;

    @NotNull
    private String createdBy;

    private Boolean activated;
    private Boolean blocked;

    private String userOwner;
    private String taxCode;
    private String firstNameOwner;
    private String lastNameOwner;
    private String emailOwner;
    private Boolean statusOwner;

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @NotNull
    private Instant createdDate;

    @Size(max = 50)
    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public String getType() {
        return type;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getEmailOwner() {
        return emailOwner;
    }

    public void setEmailOwner(String emailOwner) {
        this.emailOwner = emailOwner;
    }

    public String getFirstNameOwner() {
        return firstNameOwner;
    }

    public void setFirstNameOwner(String firstNameOwner) {
        this.firstNameOwner = firstNameOwner;
    }

    public String getLastNameOwner() {
        return lastNameOwner;
    }

    public void setLastNameOwner(String lastNameOwner) {
        this.lastNameOwner = lastNameOwner;
    }

    public Boolean getActivated() {
        return activated;
    }

    public Boolean getStatusOwner() {
        return statusOwner;
    }

    public void setStatusOwner(Boolean statusOwner) {
        this.statusOwner = statusOwner;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(String userOwner) {
        this.userOwner = userOwner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getOrgIn() {
        return orgIn;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public void setOrgIn(String orgIn) {
        this.orgIn = orgIn;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrganizationDTO organizationDTO = (OrganizationDTO) o;
        if (organizationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrganizationDTO{" + "id=" + getId() + ", name='" + getName() + "'" + ", custId=" + getCustId()
            + ", folderName='" + getFolderName() + "'" + ", folderPath='" + getFolderPath() + "'" + ", parentId="
            + getParentId() + ", orgIn='" + getOrgIn() + "'" + ", folderId='" + getFolderId() + "'"
            + ", createdBy='" + getCreatedBy() + "'" + ", createdDate='" + getCreatedDate() + "'"
            + ", lastModifiedBy='" + getLastModifiedBy() + "'" + ", lastModifiedDate='" + getLastModifiedDate()
            + "'" + "}";
    }
}

