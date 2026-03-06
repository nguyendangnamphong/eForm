package com.fis.fpt.uaadomain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable, Comparable<Document> {

    private static final long serialVersionUID = 6847692570116900089L;

    public Document() {
    }

    public Document(String id, String name, String type, Integer order, String link, Boolean internal) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
        this.link = link;
        this.internal = internal;
    }

    private String id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    @Min(value = 1)
    @Max(value = 99)
    private Integer order;

    // @JsonIgnore
    @NotNull
    private String link;

    private String location;

    private String imageDesLink;

    private Boolean internal = false;

    private Boolean isEcontractObjectStorageUpload = false;


    private List<ObjectStorageData> objectStorageDatas= new ArrayList<ObjectStorageData>();


    public Boolean getIsEcontractObjectStorageUpload() {
        return isEcontractObjectStorageUpload;
    }

    public void setIsEcontractObjectStorageUpload(Boolean isEcontractObjectStorageUpload) {
        this.isEcontractObjectStorageUpload = isEcontractObjectStorageUpload;
    }

    public List<ObjectStorageData> getObjectStorageDatas() {
        return objectStorageDatas;
    }

    public void setObjectStorageDatas(List<ObjectStorageData> objectStorageDatas) {
        this.objectStorageDatas = objectStorageDatas;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageDesLink() {
        return imageDesLink;
    }

    public void setImageDesLink(String imageDesLink) {
        this.imageDesLink = imageDesLink;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public static enum Type {
        DEFAULT("DEFAULT"), ATTACHED("ATTACHED"), ATTACHED_AFTER("ATTACHED_AFTER");

        private String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Document other = (Document) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (link == null) {
            if (other.link != null)
                return false;
        } else if (!link.equals(other.link))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (order == null) {
            if (other.order != null)
                return false;
        } else if (!order.equals(other.order))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Document{" + "id=" + getId() + ", name='" + getName() + "'" + ", type='" + getType() + "'" + ", link='"
            + getLink() + "'" + ", order=" + getOrder() + "}";
    }

    @Override
    public int compareTo(Document o) {

        int c;

        if (getId() == null || o.getId() == null) {
            return 0;
        } else {
            c = getId().compareTo(o.getId());
        }

        return c;

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
