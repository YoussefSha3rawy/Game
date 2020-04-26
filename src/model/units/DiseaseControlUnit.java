package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.disasters.Infection;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location,
			int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);
		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getToxicity() > 0) {
			target.setToxicity(target.getToxicity() - getTreatmentAmount());
			if (target.getToxicity() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getToxicity() == 0)
			heal();

	}

	public void respond(Rescuable r)throws IncompatibleTargetException,
	CannotTreatException {
		Disaster d=r.getDisaster();
		if (r instanceof Citizen) {
			if(d!=null && d instanceof Infection){
			
			
				if (this.canTreat(r)) {
						super.respond(r);
					} else
					throw new CannotTreatException(this, r,
							"This citizen is already safe");
			} else
				throw new CannotTreatException(this,r,"Disease control units only respond to Infections");

			}else
				throw new IncompatibleTargetException(this, r,
						"Medical Units can only respond to citizens");

		} 
	public String toString()
	{
		
		return super.toString()+"Unit's type : Disease Control Unit";
	}
	public String getName() {
		return "DiseaseControlUnit";
	}
}
