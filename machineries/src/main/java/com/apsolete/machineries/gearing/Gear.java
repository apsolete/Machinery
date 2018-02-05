package com.apsolete.machineries.gearing;

public abstract class Gear
{
    // common data
    private double _module;
    private int _teethNumber;
    private double _tiltAngle;
    private double _displacementFactor;
    private double _width;
    // calculated data
    private double _diameter;
    private double _diameterTops;
    private double _diameterCavities;

    public Gear(int teeth, double module, double tiltAngle)
    {
        _teethNumber = teeth;
        _module = module;
        _tiltAngle = tiltAngle;
        calculate();
    }

    public void setModule(double value)
    {
        if (_module == value)
            return;
        _module = value;
        calculate();
    }

    public double getModule()
    {
        return _module;
    }

    public void setTeethNumber(int value)
    {
        if (_teethNumber == value)
            return;
        _teethNumber = value;
        calculate();
    }

    public int getTeethNumber()
    {
        return _teethNumber;
    }

    public void setTiltAngle(double value)
    {
        if (_tiltAngle == value)
            return;
        _tiltAngle = value;
        calculate();
    }

    public double getTiltAngle()
    {
        return _tiltAngle;
    }

    public void setDisplacementFactor(double value)
    {
        if (_displacementFactor == value)
            return;
        _displacementFactor = value;
        calculate();
    }

    public double getDisplacementFactor()
    {
        return _displacementFactor;
    }

    public void setWidth(double value)
    {
        if (_width == value)
            return;
        _width = value;
        calculate();
    }

    public double getWidth()
    {
        return _width;
    }

    public double getDiameter()
    {
        return _diameter;
    }

    public double getDiameterTops()
    {
        return _diameterTops;
    }

    public double getDiameterCavities()
    {
        return _diameterCavities;
    }

    private void calculate()
    {
        if (_module <= 0 && _teethNumber <= 0)
            return;

        _diameter = _module * _teethNumber;
        _diameterTops = _diameter + 2 * _module;
        _diameterCavities = _diameter - 2.5 * _module;
    }
}
