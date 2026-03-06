package com.fis.fpt.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ApiInfo.
 */
@Entity
@Table(name = "api_info")
public class ApiInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "domain")
    private String domain;

    @Column(name = "path")
    private String path;

    @Column(name = "header")
    private String header;

    @Column(name = "request")
    private String request;

    @Column(name = "response_api")
    private String responseAPI;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public ApiInfo domain(String domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public ApiInfo path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHeader() {
        return header;
    }

    public ApiInfo header(String header) {
        this.header = header;
        return this;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getRequest() {
        return request;
    }

    public ApiInfo request(String request) {
        this.request = request;
        return this;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponseAPI() {
        return responseAPI;
    }

    public ApiInfo responseAPI(String responseAPI) {
        this.responseAPI = responseAPI;
        return this;
    }

    public void setResponseAPI(String responseAPI) {
        this.responseAPI = responseAPI;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ApiInfo createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public ApiInfo createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiInfo)) {
            return false;
        }
        return id != null && id.equals(((ApiInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApiInfo{" +
            "id=" + getId() +
            ", domain='" + getDomain() + "'" +
            ", path='" + getPath() + "'" +
            ", header='" + getHeader() + "'" +
            ", request='" + getRequest() + "'" +
            ", responseAPI='" + getResponseAPI() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            "}";
    }
}
