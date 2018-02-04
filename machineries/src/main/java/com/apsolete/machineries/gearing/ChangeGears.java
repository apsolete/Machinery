package com.apsolete.machineries.gearing;

import java.util.*;

public class ChangeGears
{
    //private HashSet<Integer>
    private HashSet<Integer> _gs0 = new HashSet<>();
    private HashSet<Integer> _gs1 = new HashSet<>();
    private HashSet<Integer> _gs2 = new HashSet<>();
    private HashSet<Integer> _gs3 = new HashSet<>();
    private HashSet<Integer> _gs4 = new HashSet<>();
    private HashSet<Integer> _gs5 = new HashSet<>();
    private HashSet<Integer> _gs6 = new HashSet<>();
    private List<HashSet<Integer>> _gs = new ArrayList<>(Arrays.asList(_gs0, _gs1, _gs2, _gs3, _gs4, _gs5, _gs6));

    public ChangeGears()
    {
    }

    public List<Integer> getGearSet(int set)
    {
        return new ArrayList<Integer>(_gs.get(set));
    }

    public void addGear(int set, int z)
    {
        HashSet<Integer> gs = _gs.get(set);
        if (!gs.contains(z))
            gs.add(z);
    }
    public void removeGear(int set, int z)
    {
        HashSet<Integer> gs = _gs.get(set);
        if (gs.contains(z))
            gs.remove(z);
    }
}
