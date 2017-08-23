package com.codepath.simpletodo;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * Created by culgnol on 8/22/17.
 */

@Table(database = MyDatabase.class)
public class TodoModel extends BaseModel {
    @Column
    @PrimaryKey
    int id;

    @Column
    int listPos;

    @Column
    String textBody;

    @Column
    String createdOn;

    public void setListPos(int pos){
        this.listPos = pos;
    }

    public void setTextBody(String text){
        this.textBody = text;
    }
}
