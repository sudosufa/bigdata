package alert;
import model.ProgramedAlert;
import model.Tracking;


public class GasAlert {
    private String observation = "";
    public void alert(ProgramedAlert programedAlert, Tracking current) {

        if (checkGasAlert(programedAlert, current)) {
            System.out.println(observation);
        }
    }
    private boolean checkGasAlert(ProgramedAlert programedAlert, Tracking current) {
        // TODO: 10/23/2020
        return false;
    }
}
