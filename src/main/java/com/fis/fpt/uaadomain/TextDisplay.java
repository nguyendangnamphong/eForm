package com.fis.fpt.uaadomain;

import java.io.Serializable;

public class TextDisplay implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -522317229448503078L;

    private Boolean signedBy = true;

    private Boolean title = true;

    private Boolean signingDate = true;

    public Boolean getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(Boolean signedBy) {
        this.signedBy = signedBy;
    }

    public Boolean getTitle() {
        return title;
    }

    public void setTitle(Boolean title) {
        this.title = title;
    }

    public Boolean getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(Boolean signingDate) {
        this.signingDate = signingDate;
    }




}
