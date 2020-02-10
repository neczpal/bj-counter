package net.neczpal.bjcounter.countings;

public enum CountingType {
    CANFIELD_EXPERT                 (true, new double[]{0,0,1,1,1,1,1,0,-1,-1}),
    CANFIELD_MASTER                 (true, new double[]{0,1,1,2,2,2,1,0,-1,-2}),
    HI_LO                           (true, new double[]{-1,1,1,1,1,1,0,0,0,-1}),
    HI_LO_OPT_1                     (true, new double[]{0,0,1,1,1,1,0,0,0,-1}),
    HI_LO_OPT_2                     (true, new double[]{0,1,1,2,2,1,1,0,0,-2}),
    KISS_2                          (false, new double[]{0,.5,1,1,1,1,0,0,0,-1}),//#TODO Red/Black
    KISS_3                          (false, new double[]{-1,.5,1,1,1,1,1,0,0,-1}),//#TODO Red/Black
    K_O                             (false, new double[]{-1,1,1,1,1,1,1,0,0,-1}),
    MENTOR                          (true, new double[]{-1,1,2,2,2,2,1,0,-1,-2}),
    OMEGA_2                         (true, new double[]{0,1,1,2,2,2,1,0,-1,-2}),
    RED_SEVEN                       (false, new double[]{-1,1,1,1,1,1,.5,0,0,-1}),//#TODO Red/Black
    REKO                            (false, new double[]{-1,1,1,1,1,1,1,0,0,-1}),
    REVERE_ADV_PLUS_MINUS           (true, new double[]{0,1,1,1,1,1,0,0,-1,-1}),
    REVERE_POINT_COUNT              (true, new double[]{-2,1,2,2,2,2,1,0,0,-2}),
    REVERE_RAPC                     (true, new double[]{-4,2,3,3,4,3,2,0,-1,-3}),
    REVERE_14_COUNT                 (true, new double[]{0,2,2,3,4,2,1,0,-2,-3}),
    SILVER_FOX                      (true, new double[]{-1,1,1,1,1,1,1,0,-1,-1}),
    UBZ_2                           (false, new double[]{-1,1,2,2,2,2,1,0,0,-2}),
    USTON_ADV_PLUS_MINUS            (true, new double[]{-1,0,1,1,1,1,1,0,0,-1}),
    USTON_APC                       (true, new double[]{0,1,2,2,3,2,2,1,-1,-3}),
    USTON_SS                        (false, new double[]{-2,2,2,2,3,2,1,0,-1,-2}),
    WONG_HALVES                     (true, new double[]{-1,.5,1,1,1,5,1,.5,0,-.5,-1}),
    ZEN_COUNT                       (true, new double[]{-1,1,1,2,2,2,1,0,0,-2}),

    INSURANCE_COUNT                 (false, new double[]{4,4,4,4,4,4,4,4,4,-9});

    private boolean isBalanced;
    private double[] values;

    CountingType(boolean isBalanced, double[] values) {
        this.isBalanced = isBalanced;
        this.values = values;
    }

    public boolean isBalanced () {
        return isBalanced;
    }

    public double getValue (int index) {
        return values[index];
    }
}











