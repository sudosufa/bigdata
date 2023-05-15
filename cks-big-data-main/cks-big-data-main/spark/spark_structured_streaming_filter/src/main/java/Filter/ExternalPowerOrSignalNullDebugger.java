package Filter;
import model.Tracking;
public class ExternalPowerOrSignalNullDebugger {
    public ExternalPowerOrSignalNullDebugger() {
    }
    public boolean ExternalPowerOrSignalNull(Tracking tracking) {
        return tracking.getExternal_power() == null || tracking.getEtatsignal() == null;
    }
}
