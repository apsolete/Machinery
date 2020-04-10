package com.apsolete.machinery.calculation.gearing.changegears;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "changegears")
public class ChgEntity
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String description;
    @ColumnInfo
    public Integer count;
    @ColumnInfo
    public String set0;
    @ColumnInfo
    public String set1;
    @ColumnInfo
    public String set2;
    @ColumnInfo
    public String set3;
    @ColumnInfo
    public String set4;
    @ColumnInfo
    public String set5;
    @ColumnInfo
    public String set6;
}
