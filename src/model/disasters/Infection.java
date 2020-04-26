package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.people.Citizen;

public class Infection extends Disaster {

	public Infection(int startCycle, Citizen target) {
		super(startCycle, target);
	}

	@Override
	public void strike() throws CitizenAlreadyDeadException,
			BuildingAlreadyCollapsedException {
		Citizen target = (Citizen) getTarget();
		if (target.isDead()) {
			throw new CitizenAlreadyDeadException(this,
					"Citizen is already dead");
		} else {
			target.setToxicity(target.getToxicity() + 25);
			super.strike();
		}

	}

	@Override
	public void cycleStep() {
		Citizen target = (Citizen) getTarget();
		target.setToxicity(target.getToxicity() + 15);

	}
	public String toString()
	{
		return "Infection Disaster has struck "+((Citizen)this.getTarget()).getName()+" at: (" + this.getTarget().getLocation().getX() + ","
				+ this.getTarget().getLocation().getY() + ")" + " during cycle " + this.getStartCycle();
	}

}
