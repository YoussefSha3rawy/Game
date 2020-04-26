package controller;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulator;
import view.StartWindow;
import view.View;

public class CommandCenter implements SOSListener, ActionListener {

	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;
	private int allCitizens;

	private View view;
	private StartWindow start;

	private ArrayList<JButton> buildings;
	private ArrayList<JButton> citizens;
	private ArrayList<JButton> units;

	// private JButton firstClick;
	// private JButton secondClick;
	private Rescuable r;
	private Unit unit;

	private ArrayList<String> log;
	private ArrayList<String> activeDisasters;
	private ArrayList<JButton> activeUnits;

	public CommandCenter() throws Exception {
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		emergencyUnits = engine.getEmergencyUnits();
		allCitizens = 0;
		view = new View();
		start = new StartWindow();
		start.getStart().addActionListener(this);
		buildings = new ArrayList<>();
		citizens = new ArrayList<>();
		units = new ArrayList<>();
		log = new ArrayList<>();
		activeDisasters = new ArrayList<>();
		activeUnits = new ArrayList<>();
		JButton respond = new JButton();
		respond.addActionListener(this);
		respond.setText("Respond");
		Font titleFont = new Font(Font.DIALOG, Font.BOLD, 30);
		respond.setFont(titleFont);
		respond.setBackground(Color.BLACK);
		respond.setForeground(Color.RED);
		view.getLeft().add(respond);
		JButton next = new JButton();
		ImageIcon n = new ImageIcon("Next.png");
		next.setText("Next Cycle");
		next.setIcon(n);
		next.setBackground(Color.GREEN);
		next.setFont(titleFont);
		next.setVerticalTextPosition(SwingConstants.TOP);
		next.setHorizontalTextPosition(SwingConstants.CENTER);
		next.addActionListener(this);
		view.getMainButtons().add(next);
		JButton recommend = new JButton("Recommend an action");
		recommend.setBackground(Color.YELLOW);
		recommend.setForeground(Color.BLUE);
		recommend.setFont(titleFont);
		recommend.setVerticalTextPosition(SwingConstants.TOP);
		recommend.setHorizontalTextPosition(SwingConstants.CENTER);
		recommend.addActionListener(this);
		view.getMainButtons().add(recommend);
		view.updateLog(log);
		view.updateActiveDisasters(activeDisasters);

		for (int i = 0; i < this.emergencyUnits.size(); i++) {
			JButton b = new JButton();
			b.setBackground(Color.WHITE);
			b.setText(this.emergencyUnits.get(i).getUnitID());
			if (this.emergencyUnits.get(i) instanceof Ambulance) {
				b.setText("AMB " + this.emergencyUnits.get(i).getUnitID());
				b.setIcon(new ImageIcon("ambulance.png"));
				b.setToolTipText("Ambulance");

			}
			if (this.emergencyUnits.get(i) instanceof DiseaseControlUnit) {
				b.setText("DCU " + this.emergencyUnits.get(i).getUnitID());
				b.setIcon(new ImageIcon("DCU.png"));
				b.setToolTipText("Disease Control Unit");

			}
			if (this.emergencyUnits.get(i) instanceof Evacuator) {
				b.setText("EVC " + this.emergencyUnits.get(i).getUnitID());
				b.setIcon(new ImageIcon("evacuator.png"));
				b.setToolTipText("Evacuator");

			}
			if (this.emergencyUnits.get(i) instanceof FireTruck) {
				b.setText("FTK " + this.emergencyUnits.get(i).getUnitID());
				b.setIcon(new ImageIcon("FireTruck.png"));
				b.setToolTipText("Fire Truck");

			}
			if (this.emergencyUnits.get(i) instanceof GasControlUnit) {
				b.setText("GCU " + this.emergencyUnits.get(i).getUnitID());
				b.setIcon(new ImageIcon("GCU.png"));
				b.setToolTipText("Gas Control Unit");
			}
			b.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			b.setHorizontalAlignment(SwingConstants.LEFT);
			b.setHorizontalTextPosition(SwingConstants.RIGHT);
			b.addActionListener(this);
			b.setMinimumSize(new Dimension(400, 400));
			b.setBackground(Color.ORANGE);
			units.add(b);
			view.addUnit(b);
		}
		view.updateGeneralInfo(0);
	}

	@Override
	public void receiveSOSCall(Rescuable r) {
		log.add(r.getDisaster().toString());
		if (r instanceof ResidentialBuilding) {
			if (!visibleBuildings.contains(r)) {
				allCitizens += ((ResidentialBuilding) r).getOccupants().size();
				visibleBuildings.add((ResidentialBuilding) r);
				JButton b = new JButton();
				b.setIcon(new ImageIcon("building.png"));
				b.setBackground(Color.BLACK);
				b.addActionListener(this);
				b.setText(null);
				b.setForeground(Color.white);
				b.setToolTipText("Building in trouble");
				b.setBorderPainted(false);
				view.addCell(b, r.getLocation().getX(), r.getLocation().getY());
				buildings.add(b);
			}

		} else {

			if (!visibleCitizens.contains(r)) {
				allCitizens++;
				visibleCitizens.add((Citizen) r);
				JButton b = new JButton();
				b.setIcon(new ImageIcon("citizen.png"));
				b.setBackground(Color.BLACK);
				b.addActionListener(this);
				b.setText(null);
				b.setForeground(Color.white);
				b.setBorderPainted(false);
				b.setToolTipText("Citizen in trouble");
				view.addCell(b, r.getLocation().getX(), r.getLocation().getY());
				citizens.add(b);
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (start.getStart().equals((JButton) (e.getSource()))) {
			start.setVisible(false);
			view.setVisible(true);
		}
		if (!engine.checkGameOver()) {
			JButton b = (JButton) e.getSource();
			String s = "";
			if (b.getText() != null && b.getText().equals("Recommend an action"))
				recommend();
			if (b.getText() != null && b.getText().equals("Next Cycle")) {
				unit = null;
				r = null;
				view.updateInfo("");
				engine.nextCycle();
				view.updateGeneralInfo(engine.calculateCasualties());
				for (int i = 0; i < visibleCitizens.size(); i++) {
					if (visibleCitizens.get(i).getState() == CitizenState.DECEASED) {
						if (!log.contains((visibleCitizens.get(i).getName() + " has died at ("
								+ visibleCitizens.get(i).getLocation().getX() + ","
								+ visibleCitizens.get(i).getLocation().getY() + ")"))) {
							log.add(visibleCitizens.get(i).getName() + " has died at ("
									+ visibleCitizens.get(i).getLocation().getX() + ","
									+ visibleCitizens.get(i).getLocation().getY() + ")");
						}
					}
				}
				for (int i = 0; i < visibleBuildings.size(); i++) {
					if (visibleBuildings.get(i).getStructuralIntegrity() == 0) {
						String y = "The building at (" + visibleBuildings.get(i).getLocation().getX() + ","
								+ visibleBuildings.get(i).getLocation().getY() + ") has collapsed";
						if (!visibleBuildings.get(i).getOccupants().isEmpty()) {
							s += " killing the following citizens inside it:" + "\n";
							for (Citizen c : visibleBuildings.get(i).getOccupants()) {
								y += c.getName() + "\n";
							}
						}
						if (!log.contains(y)) {
							log.add(y);
						}
					}
				}
				view.updateLog(log);
				while (!activeDisasters.isEmpty()) {
					activeDisasters.remove(0);
				}
				for (Citizen c : visibleCitizens) {
					if (c.getDisaster().isActive())
						activeDisasters.add(c.getDisaster().toString());
				}
				for (ResidentialBuilding c : visibleBuildings) {
					if (c.getDisaster().isActive())
						activeDisasters.add(c.getDisaster().toString());
				}
				view.updateActiveDisasters(activeDisasters);
				while (!activeUnits.isEmpty()) {
					JButton i = activeUnits.remove(0);
					i.repaint();
					view.getLeft().repaint();
				}
				for (Unit u : emergencyUnits) {
					if (u.getState() == UnitState.RESPONDING || u.getState() == UnitState.TREATING) {
						JButton a = units.get(emergencyUnits.indexOf(u));
						a.setBackground(Color.RED);
						view.removeUnit(a);
						a.repaint();
						view.getLeft().repaint();
						activeUnits.add(a);
					}
					if (u.getState() == UnitState.IDLE) {
						JButton a = units.get(emergencyUnits.indexOf(u));
						a.setBackground(Color.ORANGE);
						a.repaint();
						view.getLeft().repaint();
						view.addUnit(a);
					}
				}
				view.updateActiveUnits(activeUnits);
				for (Citizen c : visibleCitizens) {
					if (c.isDead()) {
						int x = c.getLocation().getX();
						int y = c.getLocation().getY();
						JButton d = view.getButtons()[x][y];
						d.setIcon(new ImageIcon("RIP.png"));
					}
				}
				for (ResidentialBuilding c : visibleBuildings) {
					if (c.getStructuralIntegrity() == 0) {
						int x = c.getLocation().getX();
						int y = c.getLocation().getY();
						JButton d = view.getButtons()[x][y];
						d.setIcon(new ImageIcon("collapsed.png"));

					}
				}

				s = this.updateUnitsLocation(s);

			}
			int x = 0;
			int y = 0;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					if (b.equals(view.getButtons()[i][j])) {
						x = i;
						y = j;
					}
				}
			}
			for (int i = 0; i < visibleBuildings.size(); i++) {
				if (visibleBuildings.get(i).getLocation().getX() == x
						&& visibleBuildings.get(i).getLocation().getY() == y) {
					s += visibleBuildings.get(i).toString() + "___________________________" + "\n";
					r = visibleBuildings.get(i);
				}

			}
			for (int i = 0; i < visibleCitizens.size(); i++) {
				if (visibleCitizens.get(i).getLocation().getX() == x
						&& visibleCitizens.get(i).getLocation().getY() == y) {
					s += visibleCitizens.get(i).toString() + "___________________________" + "\n";
					r = visibleCitizens.get(i);
				}

			}
			if (b.getText() != null && (b.getText().charAt(b.getText().length() - 1) >= '0'
					&& b.getText().charAt(b.getText().length() - 1) <= '9')) {
				int index = units.indexOf(b);
				Unit u = emergencyUnits.get(index);
				s += u.toString();
				unit = u;
			}
			if (b.getText() != null && b.getText().equals("Respond")) {
				if (unit == null)
					view.displayPopUp("Please select a unit");
				if (r == null)
					view.displayPopUp("Please select a target");
				if (unit != null && r != null) {
					try {
						unit.respond(r);
						s += "Selected Unit is now responding";
					} catch (IncompatibleTargetException | CannotTreatException e1) {
						view.displayPopUp(e1.getMessage());
					}
				}
			}
			view.updateInfo(s);
			// firstClick = secondClick;
			// secondClick = view.getButtons()[x][y];
		} else {
			view.endGame(engine.calculateCasualties(), allCitizens);
		}
	}

	public String updateUnitsLocation(String s) {
		for (int i = 0; i < emergencyUnits.size(); i++) {
			if (emergencyUnits.get(i).getTarget() != null) {
				if (emergencyUnits.get(i).arrived()) {
					Rescuable r = emergencyUnits.get(i).getTarget();
					if (r instanceof ResidentialBuilding) {
						int index = visibleBuildings.indexOf((ResidentialBuilding) r);
						JButton b = buildings.get(index);
						if (emergencyUnits.get(i) instanceof Evacuator) {
							b.setIcon(new ImageIcon("EvacuatorBuilding.png"));
						}
						if (emergencyUnits.get(i) instanceof FireTruck) {
							b.setIcon(new ImageIcon("FireTruckBuilding.png"));
						}
						if (emergencyUnits.get(i) instanceof GasControlUnit) {
							b.setIcon(new ImageIcon("GCUBuilding.png"));
						}
						view.addCell(b, r.getLocation().getX(), r.getLocation().getY());
						s = "UNIT ARRIVED AT (" + r.getLocation().getX() + "," + r.getLocation().getY() + ")";
					} else {
						int index = visibleCitizens.indexOf((Citizen) r);
						JButton b = citizens.get(index);
						if (emergencyUnits.get(i) instanceof Ambulance) {
							b.setIcon(new ImageIcon("AmbulanceCitizen.png"));
						}
						if (emergencyUnits.get(i) instanceof DiseaseControlUnit) {
							b.setIcon(new ImageIcon("DCUCitizen.png"));
						}
						view.addCell(b, r.getLocation().getX(), r.getLocation().getY());
						s = "UNIT ARRIVED AT (" + r.getLocation().getX() + "," + r.getLocation().getY() + ")";
					}
				}
			}
		}
		boolean evacBase = false;
		for (Unit u : emergencyUnits) {
			if (u instanceof Evacuator) {
				if (u.getLocation().getX() == 0 && u.getLocation().getY() == 0 && u.getState() != UnitState.IDLE) {
					JButton bn = view.getButtons()[0][0];
					bn.setIcon(new ImageIcon("evacuatorBase.png"));
					JButton bm = view.getButtons()[u.getTarget().getLocation().getX()][u.getTarget().getLocation()
							.getY()];
					bm.setIcon(new ImageIcon("building.png"));
				}
			}
		}
		for (Unit u : emergencyUnits) {
			if (u instanceof Evacuator) {
				if (u.getLocation().getX() == 0 && u.getLocation().getY() == 0 && u.getState() != UnitState.IDLE) {
					evacBase = true;
				}
			}
		}
		if (evacBase == false) {
			JButton bn = view.getButtons()[0][0];
			bn.setIcon(new ImageIcon("base.png"));
			view.addCell(bn, 0, 0);
		}
		for (ResidentialBuilding b : visibleBuildings) {
			if (!b.getDisaster().isActive()) {
				int index = visibleBuildings.indexOf(b);
				JButton bn = buildings.get(index);
				if (b.getStructuralIntegrity() == 0) {
					bn.setIcon(new ImageIcon("collapsed.png"));
				} else {
					bn.setIcon(new ImageIcon("building.png"));
				}
				view.addCell(bn, b.getLocation().getX(), b.getLocation().getY());
			}
		}
		for (Citizen b : visibleCitizens) {
			if (!b.getDisaster().isActive()) {
				int index = visibleCitizens.indexOf(b);
				JButton bn = citizens.get(index);
				if (b.getState() == CitizenState.DECEASED) {
					bn.setIcon(new ImageIcon("RIP.png"));
				} else {
					bn.setIcon(new ImageIcon("citizen.png"));
				}
				view.addCell(bn, b.getLocation().getX(), b.getLocation().getY());
			}
		}
		return s;

	}

	public void recommend() {
		String r = "";
		ArrayList<Citizen> CIT = new ArrayList<>();
		ArrayList<ResidentialBuilding> BIT = new ArrayList<>();
		ArrayList<Unit> ActiveUnits = new ArrayList<>();
		ArrayList<Unit> IdleUnits = new ArrayList<>();
		for (Citizen c : visibleCitizens) {
			if (c.getDisaster().isActive())
				CIT.add(c);
			/*
			 * else { if (c.getHp() <= 30) r += "Send a DCU or an ambulance to heal " +
			 * c.getName() + " located at (" + c.getLocation().getX() + "," +
			 * c.getLocation().getY() + ")" + "as his HP level is low." + "\n"; }
			 */
		}
		for (ResidentialBuilding c : visibleBuildings) {
			if (c.getDisaster().isActive())
				BIT.add(c);
			/*
			 * else { if (c.getStructuralIntegrity() <= 30) r +=
			 * "Evacuate the building at (" + c.getLocation().getX() + "," +
			 * c.getLocation().getY() + ")" + "as its structural integrity level is low." +
			 * "\n"; }
			 */
		}
		for (Unit u : emergencyUnits) {
			if (u.getTarget() == null) {
				IdleUnits.add(u);
			} else {
				ActiveUnits.add(u);
			}
		}
		for (Citizen c : CIT) {
			if (c.getDisaster() instanceof Injury && c.getState() != CitizenState.DECEASED) {
				boolean hasIdleAmb = false;
				for (Unit u : IdleUnits) {
					if (u instanceof Ambulance) {
						int cycles = (int) Math
								.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
						int cyclesLeft = (int) Math.ceil((100-c.getBloodLoss()) / 10);
						if (cyclesLeft > cycles) {
							r += "Send an ambulance to treat " + c.getName() + " located at (" + c.getLocation().getX()
									+ "," + c.getLocation().getY() + ")" + " as he has an injury." + "\n";
							hasIdleAmb = true;
						}
					}

				}
				if (hasIdleAmb == false) {
					for (Unit u : ActiveUnits) {
						if (u instanceof Ambulance) {
							if (((Citizen) u.getTarget()).getBloodLoss() < c.getBloodLoss()) {
								int cycles = (int) Math
										.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
								int cyclesLeft = (int) Math.ceil((100-c.getBloodLoss()) / 10);
								if (cyclesLeft > cycles) {
									r += "Redirect the active ambulance with ID: " + u.getUnitID() + " to respond to "
											+ c.getName() + " at (" + c.getLocation().getX() + ","
											+ c.getLocation().getY() + ") as his blood loss(" + c.getBloodLoss()
											+ ") is higher than the current target's blood loss("
											+ ((Citizen) u.getTarget()).getBloodLoss() + ")" + "\n";
								}
							}
						}
					}
				}
			}
			if (c.getDisaster() instanceof Infection && c.getState() != CitizenState.DECEASED) {
				boolean hasIdleAmb = false;
				for (Unit u : IdleUnits) {
					if (u instanceof DiseaseControlUnit) {
						int cycles = (int) Math
								.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
						int cyclesLeft = (int) Math.ceil((100-c.getToxicity()) / 15);
						if (cyclesLeft > cycles) {
							r += "Send a DCU to treat " + c.getName() + " located at (" + c.getLocation().getX() + ","
									+ c.getLocation().getY() + ")" + " as he has an infection." + "\n";
							hasIdleAmb = true;
						}
					}
				}
				if (hasIdleAmb == false) {
					for (Unit u : ActiveUnits) {
						if (u instanceof DiseaseControlUnit) {
							if (((Citizen) u.getTarget()).getToxicity() < c.getToxicity()) {
								int cycles = (int) Math
										.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
								int cyclesLeft = (int) Math.ceil((100-c.getToxicity()) / 15);
								if (cyclesLeft > cycles) {
									r += "Redirect the active DCU with ID: " + u.getUnitID() + " to respond to "
											+ c.getName() + " at (" + c.getLocation().getX() + ","
											+ c.getLocation().getY() + ") as his toxicity level(" + c.getToxicity()
											+ ") is higher than the current target's toxicity level("
											+ ((Citizen) u.getTarget()).getToxicity() + ")" + "\n";
								}
							}
						}
					}
				}

			}
		}
		for (ResidentialBuilding c : BIT) {
			if (c.getDisaster() instanceof Collapse && c.getStructuralIntegrity() != 0) {
				boolean hasIdleAmb = false;
				for (Unit u : IdleUnits) {
					if (u instanceof Evacuator) {
						int cycles = (int) Math
								.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
						int cyclesLeft = (int) Math.ceil((100-c.getFoundationDamage()) / 10);
						if (cyclesLeft > cycles) {
							r += "Send an evacuator to treat the building located at (" + c.getLocation().getX() + ","
									+ c.getLocation().getY() + ")" + " as a collapse disaster has striked it." + "\n";
							hasIdleAmb = true;
						}
					}
				}
				if (hasIdleAmb == false) {
					for (Unit u : ActiveUnits) {
						if (u instanceof Evacuator) {
							if (((ResidentialBuilding) u.getTarget()).getFoundationDamage() < c.getFoundationDamage()) {
								int cycles = (int) Math
										.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
								int cyclesLeft = (int) Math.ceil((100-c.getFoundationDamage()) / 10);
								if (cyclesLeft > cycles) {
									r += "Redirect the active evacuator with ID: " + u.getUnitID()
											+ " to respond to the building at (" + c.getLocation().getX() + ","
											+ c.getLocation().getY() + ") as its foundation damage("
											+ c.getFoundationDamage()
											+ ") is higher than the current target's foundation damage("
											+ ((ResidentialBuilding) u.getTarget()).getFoundationDamage() + ")";
								}
							}
						}
					}
				}
			}
			if (c.getDisaster() instanceof Fire && c.getStructuralIntegrity() != 0) {
				boolean hasIdleAmb = false;
				for (Unit u : IdleUnits) {
					if (u instanceof FireTruck) {
						int cycles = (int) Math
								.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
						int cyclesLeft = (int) Math.ceil((100-c.getFireDamage()) / 10);
						if (cyclesLeft > cycles) {
							r += "Send a firetruck to treat the building located at (" + c.getLocation().getX() + ","
									+ c.getLocation().getY() + ")" + " as a fire disaster has striked it." + "\n";
							hasIdleAmb = true;
						}
					}
				}
				if (hasIdleAmb == false) {
					for (Unit u : ActiveUnits) {
						if (u instanceof FireTruck) {
							if (((ResidentialBuilding) u.getTarget()).getFireDamage() < c.getFireDamage()) {
								int cycles = (int) Math
										.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
								int cyclesLeft = (int) Math.ceil((100-c.getFireDamage()) / 10);
								if (cyclesLeft > cycles) {
									r += "Redirect the active firetruck with ID: " + u.getUnitID()
											+ " to respond to the building at (" + c.getLocation().getX() + ","
											+ c.getLocation().getY() + ") as its fire damage level(" + c.getFireDamage()
											+ ") is higher than the current target's fire damage level("
											+ ((ResidentialBuilding) u.getTarget()).getFireDamage() + ")"+"\n";
							}
							}
						}
					}
				}
			}
			if (c.getDisaster() instanceof GasLeak && c.getStructuralIntegrity() != 0) {
				boolean hasIdleAmb = false;
				for (Unit u : IdleUnits) {
					if (u instanceof GasControlUnit) {
						int cycles = (int) Math
								.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
						int cyclesLeft = (int) Math.ceil((100-c.getGasLevel()) / 15);
						if (cyclesLeft > cycles) {
							r += "Send a GCU to treat the building located at (" + c.getLocation().getX() + ","
									+ c.getLocation().getY() + ")" + " as a gas leak disaster has striked it." + "\n";
							hasIdleAmb = true;
						}
					}
				}
				if (hasIdleAmb == false) {
					for (Unit u : ActiveUnits) {
						if (u instanceof GasControlUnit) {
							if (((ResidentialBuilding) u.getTarget()).getGasLevel() < c.getGasLevel()) {
								int cycles = (int) Math
										.ceil((c.getLocation().getX() + c.getLocation().getY()) / u.getStepsPerCycle());
								int cyclesLeft = (int) Math.ceil((100-c.getGasLevel()) / 15);
								if (cyclesLeft > cycles) {
									r += "Redirect the active GCU with ID: " + u.getUnitID()
											+ " to respond to the building at (" + c.getLocation().getX() + ","
											+ c.getLocation().getY() + ") as its gas level(" + c.getGasLevel()
											+ ") is higher than the current target's gas level("
											+ ((ResidentialBuilding) u.getTarget()).getGasLevel() + ")";
								}
							}
						}
					}
				}
			}
		}
		if (r.equals(""))
			r = "No actions are currently recommended";
		view.displayPopUp(r);
	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		CommandCenter v = new CommandCenter();
	}

}
