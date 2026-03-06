package com.fis.fpt.uaadomain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "name", length = 256, nullable = false)
    private String name;

    @NotNull
    @Size(max = 256)
    @Column(name = "ac_name", length = 256, nullable = false)
    private String acName;

    @NotNull
    @Max(value = 999999999)
    @Column(name = "cust_id", nullable = false)
    private Long custId;

    @NotNull
    @Size(max = 50)
    @Column(name = "folder_name", length = 50, nullable = false)
    private String folderName;

    @NotNull
    @Size(max = 256)
    @Column(name = "folder_path", length = 256, nullable = false)
    private String folderPath;

    @Size(max = 30)
    @Column(name = "org_code", length = 30, nullable = false)
    private String orgCode;

    //	@NotNull
    @Size(max = 20)
    @Column(name = "tax_code", length = 20, nullable = false)
    private String taxCode;

    @Max(value = 999999999)
    @Column(name = "parent_id")
    private Long parentId;

    @NotNull
    @Column(name = "org_in", nullable = false)
    private String orgIn;

    @Size(max = 60)
    @Column(name = "folder_id", length = 60)
    private String folderId;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "blocked")
    private Boolean blocked = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not
    // remove
    public Long getId() {
        return id;
    }

    public Boolean getActivated() {
        return activated;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCustId() {
        return custId;
    }

    public Organization custId(Long custId) {
        this.custId = custId;
        return this;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getFolderName() {
        return folderName;
    }

    public Organization folderName(String folderName) {
        this.folderName = folderName;
        return this;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public Organization folderPath(String folderPath) {
        this.folderPath = folderPath;
        return this;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public Long getParentId() {
        return parentId;
    }

    public Organization parentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getOrgIn() {
        return orgIn;
    }

    public Organization orgIn(String orgIn) {
        this.orgIn = orgIn;
        return this;
    }

    public void setOrgIn(String orgIn) {
        this.orgIn = orgIn;
    }

    public String getFolderId() {
        return folderId;
    }

    public Organization folderId(String folderId) {
        this.folderId = folderId;
        return this;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Organization createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Organization createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Organization lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Organization lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Organization{" + "id=" + getId() + ", name='" + getName() + "'" + ", custId=" + getCustId()
            + ", folderName='" + getFolderName() + "'" + ", folderPath='" + getFolderPath() + "'" + ", parentId="
            + getParentId() + ", orgIn='" + getOrgIn() + "'" + ", folderId='" + getFolderId() + "'"
            + ", createdBy='" + getCreatedBy() + "'" + ", createdDate='" + getCreatedDate() + "'"
            + ", lastModifiedBy='" + getLastModifiedBy() + "'" + ", lastModifiedDate='" + getLastModifiedDate()
            + "'" + "}";
    }
}
