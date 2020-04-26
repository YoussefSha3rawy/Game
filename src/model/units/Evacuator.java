package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0
				|| target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity()
				&& i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX()
				+ target.getLocation().getY());

	}

	public void respond(Rescuable r) throws IncompatibleTargetException,
			CannotTreatException {
		Disaster d = r.getDisaster();
		if (r instanceof ResidentialBuilding) {

			if (d != null && d instanceof Collapse) {
				if (this.canTreat(r)
						&& !((ResidentialBuilding) r).hasCollapsed())
					super.respond(r);
				else

					throw new CannotTreatException(this, r,
							"Building doesn't suffer from any disaster");
			} else
				throw new CannotTreatException(this, r,
						" Evacuator can only respond to collapses");

		} else
			throw new IncompatibleTargetException(this, r,
					"Evacuators can only respond to residential buildings");

	}

	public String toString() {

		String y = "Unit's type : Evacuator" + "\n";
		y += "Passenger Count: " + this.getPassengers().size();
		for (int i = 0; i < this.getPassengers().size(); i++)
			y += getPassengers().get(i).toString()
					+ "Citizen is in an evacuator" + "\n";
		return super.toString() + y;
	}

	public String getName() {
		return "Evacuator";
	}

}
