package com.fis.fpt.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A VariableSystemAcl.
 */
@Entity
@Table(name = "variable_system_acl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariableSystemAcl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "var_id")
    private String varId;

    @Column(name = "perm")
    private String perm;

    @Column(name = "perm_type")
    private Long permType;

    @Column(name = "assignee_id")
    private Long assigneeId;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "authorizer_id")
    private Long authorizerId;

    @Column(name = "authorizer")
    private String authorizer;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "user_id")
    private Long userId;

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
}
