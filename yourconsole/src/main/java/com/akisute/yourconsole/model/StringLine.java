package com.akisute.yourconsole.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "StringLine")
public class StringLine extends Model {

    @Column
    private String mText;

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public static StringLine newStringLine(String line) {
        StringLine stringLine = new StringLine();
        stringLine.setText(line.replace("\n", " "));
        return stringLine;
    }

    public static List<StringLine> getAll() {
        return new Select().from(StringLine.class).orderBy("Id ASC").execute();
    }
}
