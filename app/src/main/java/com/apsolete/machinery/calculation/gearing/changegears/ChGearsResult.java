package com.apsolete.machinery.calculation.gearing.changegears;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "changegearsresult",
        foreignKeys =
                {
                        @ForeignKey(entity = ChGearsEntity.class, parentColumns = "id", childColumns = "chg_id")
                })
public class ChGearsResult
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(index = true)
    public long chg_id;
    @ColumnInfo
    public Integer number;
    @ColumnInfo
    public Double ratio;
    @ColumnInfo
    public Integer z1;
    @ColumnInfo
    public Integer z2;
    @ColumnInfo
    public Integer z3;
    @ColumnInfo
    public Integer z4;
    @ColumnInfo
    public Integer z5;
    @ColumnInfo
    public Integer z6;
}
