package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.people.Citizen;

public class Injury extends Disaster {

	public Injury(int startCycle, Citizen target) {
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

			target.setBloodLoss(target.getBloodLoss() + 30);
			super.strike();
		}

	}

	@Override
	public void cycleStep() {
		Citizen target = (Citizen) getTarget();
		target.setBloodLoss(target.getBloodLoss() + 10);

	}
public String toString()
{
		return "Injury Disaster has struck "+((Citizen)this.getTarget()).getName()+ " at: (" + this.getTarget().getLocation().getX() + ","
				+ this.getTarget().getLocation().getY() + ")" + " during cycle " + this.getStartCycle();

}

}
