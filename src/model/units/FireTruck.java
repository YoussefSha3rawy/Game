package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.disasters.Fire;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getFireDamage() > 0)

			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)

			jobsDone();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException,
			CannotTreatException {
		Disaster d = r.getDisaster();
		if (r instanceof ResidentialBuilding) {

			if (d != null && d instanceof Fire) {
				if (this.canTreat(r))
					super.respond(r);
				else
					throw new CannotTreatException(this, r,
							"Building doesn't suffer from any disaster");
			} else
				throw new CannotTreatException(this, r,
						" Fire trucks can only respond to fire");

		} else
			throw new IncompatibleTargetException(this, r,
					"Fire trucks can only respond to residential buildings");

	}

	public String toString() {

		return super.toString() + "Unit's type : FireTruck";
	}

	public String getName() {
		return "FireTruck";
	}

}
