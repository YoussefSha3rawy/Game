package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.disasters.GasLeak;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0)
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException,
			CannotTreatException {
		Disaster d = r.getDisaster();
		if (r instanceof ResidentialBuilding) {

			if (d != null && d instanceof GasLeak) {
				if (this.canTreat(r))
					super.respond(r);
				else
					throw new CannotTreatException(this, r,
							"Building doesn't suffer from any disaster");
			} else
				throw new CannotTreatException(this, r,
						" Gas Control units can only respond to gas leaks");

		} else
			throw new IncompatibleTargetException(this, r,
					"Gas Control units can only respond to residential buildings");

	}

	public String toString() {

		return super.toString() + "Unit's type : Gas Control Unit";
	}

	public String getName() {
		return "GasControlUnit";
	}

}
