package com.fis.fpt.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A StorageFile.
 */
@Entity
@Table(name = "storage_file")
public class StorageFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "bucket")
    private String bucket;

    @Column(name = "path")
    private String path;

    @Column(name = "status")
    private int status;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "org_in")
    private String orgIn;

    @Column(name = "cust_id")
    private Long custId;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "bean_name")
    private String beanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBucket() {
        return bucket;
    }

    public StorageFile bucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getPath() {
        return path;
    }

    public StorageFile path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public StorageFile status(int status) {
        this.status = status;
        return this;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public StorageFile userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public StorageFile createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOrgIn() {
        return orgIn;
    }

    public StorageFile orgIn(String orgIn) {
        this.orgIn = orgIn;
        return this;
    }

    public void setOrgIn(String orgIn) {
        this.orgIn = orgIn;
    }

    public Long getCustId() {
        return custId;
    }

    public StorageFile custId(Long custId) {
        this.custId = custId;
        return this;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public StorageFile createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StorageFile lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public StorageFile lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageFile)) {
            return false;
        }
        return id != null && id.equals(((StorageFile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageFile{" +
            "id=" + getId() +
            ", bucket='" + getBucket() + "'" +
            ", path='" + getPath() + "'" +
            ", status='" + getStatus() + "'" +
            ", userId=" + getUserId() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", orgIn='" + getOrgIn() + "'" +
            ", custId=" + getCustId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
