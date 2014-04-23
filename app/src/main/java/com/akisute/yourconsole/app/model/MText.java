package com.akisute.yourconsole.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.akisute.yourconsole.app.intent.Intents;

import java.util.Date;
import java.util.List;

@Table(name = "MText")
public class MText extends Model {

    @Column(index = true, notNull = true)
    private Date mTimestamp;
    @Column(index = true, notNull = true)
    private String mSenderPackageName;
    @Column(notNull = true)
    private String mMimeType;
    @Column
    private String mText;

    public Date getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Date mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public String getSenderPackageName() {
        return mSenderPackageName;
    }

    public void setSenderPackageName(String mSenderPackageName) {
        this.mSenderPackageName = mSenderPackageName;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setMimeType(String mMimeType) {
        this.mMimeType = mMimeType;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public static MText newInstance(String senderPackageName, String mimeType, String text) {
        if (senderPackageName == null) {
            return null;
        }
        if (mimeType == null) {
            mimeType = Intents.MIME_TYPE_PLAINTEXT;
        }
        if (text == null) {
            return null;
        }
        MText instance = new MText();
        instance.setTimestamp(new Date());
        instance.setSenderPackageName(senderPackageName);
        instance.setMimeType(mimeType);
        instance.setText(text);
        return instance;
    }

    public static List<MText> getAll() {
        return new Select().from(MText.class).orderBy("mTimestamp ASC").execute();
    }

    public static void removeAll() {
        new Delete().from(MText.class).execute();
    }
}
