package com.app.spark.Filter;

import com.app.spark.model.Tracking;

public class ExternalPowerOrSignalNullDebugger {
    public ExternalPowerOrSignalNullDebugger() {
    }
    public boolean ExternalPowerOrSignalNull(Tracking tracking) {
        return tracking.getExternal_power() == null || tracking.getEtatSignal() == null;
    }
}
