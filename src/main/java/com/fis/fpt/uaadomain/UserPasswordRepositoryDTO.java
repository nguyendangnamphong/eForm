package com.fis.fpt.uaadomain;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

public class UserPasswordRepositoryDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String type;

    private Long userId;

    @Size(max = 200)
    private String passwordHash;

    private Boolean status;

    private Long custId;

    @Size(max = 100)
    private String orgIn;

    private ZonedDateTime createdDate;

    private String createdBy;

    private ZonedDateTime lastModifiedDate;

    private String lastModifiedBy;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPasswordRepositoryDTO)) {
            return false;
        }

        return id != null && id.equals(((UserPasswordRepositoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPasswordRepositoryDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", userId=" + getUserId() +
            ", passwordHash='" + getPasswordHash() + "'" +
            ", status='" + isStatus() + "'" +
            ", custId=" + getCustId() +
            ", orgIn='" + getOrgIn() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}

