package com.hecvd19.pas.model;

public class Attachment {

    public String attachmentUrl;
    public String mimeType;

    public Attachment() {
    }

    public Attachment(String attachmentUrl, String mimeType) {
        this.attachmentUrl = attachmentUrl;
        this.mimeType = mimeType;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "attachmentUrl='" + attachmentUrl + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }
}
