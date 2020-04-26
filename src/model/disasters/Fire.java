package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;

public class Fire extends Disaster {

	public Fire(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);

	}

	@Override
	public void strike() throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.hasCollapsed()) {
			throw new BuildingAlreadyCollapsedException(this, "Building has already collapsed");
		} else {
			target.setFireDamage(target.getFireDamage() + 10);
			super.strike();
		}

	}

	@Override
	public void cycleStep() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setFireDamage(target.getFireDamage() + 10);

	}

	public String toString() {
		return "Fire Disaster has struck the Building at: (" + this.getTarget().getLocation().getX() + ","
				+ this.getTarget().getLocation().getY() + ")" + " during cycle " + this.getStartCycle();
	}

}
