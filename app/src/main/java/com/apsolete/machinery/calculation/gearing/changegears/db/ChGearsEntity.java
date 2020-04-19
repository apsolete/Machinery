package com.apsolete.machinery.calculation.gearing.changegears.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "changegears")
public class ChGearsEntity
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo
    public String name;
    @ColumnInfo
    public String description;
    @ColumnInfo
    public Double accuracy;
    @ColumnInfo
    public Integer mode;
    @ColumnInfo
    public Double ratio;
    @ColumnInfo
    public Boolean ratioAsFraction;
    @ColumnInfo
    public Double threadPitch;
    @ColumnInfo
    public Integer threadPitchUnit;
    @ColumnInfo
    public Double leadScrewPitch;
    @ColumnInfo
    public Integer leadScrewPitchUnit;
    @ColumnInfo
    public Boolean diffLocked23;
    @ColumnInfo
    public Boolean diffLocked45;
    @ColumnInfo
    public Boolean diffGearing12;
    @ColumnInfo
    public Boolean diffGearing34;
    @ColumnInfo
    public Boolean diffGearing56;
    @ColumnInfo
    public Boolean oneSet;
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
