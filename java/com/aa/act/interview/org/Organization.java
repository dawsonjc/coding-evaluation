package com.aa.act.interview.org;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public abstract class Organization {

	private Position root;
	private int id;
	
	public Organization() {
		root = createOrganization();
	}
	
	protected abstract Position createOrganization();
	
	/**
	 * hire the given person as an employee in the position that has that title
	 * 
	 * @param person
	 * @param title
	 * @return the newly filled position or empty if no position has that title
	 */
	public Optional<Position> hire(Name person, String title) {
		if(!this.root.isFilled() && this.root.getTitle().equals(title)) {
			Employee employee = new Employee(id++, person);
			this.root.setEmployee(Optional.of(employee));
			return Optional.of(root);
		}

		// go into the tree
		if(this.root.getDirectReports().size() != 0) {
			return this.positionFinder(this.root.getDirectReports(), person, title, Optional.empty());
		}

		return Optional.empty();
	}

	private Optional<Position> positionFinder(Collection<Position> positions, Name person, String title, Optional<Position> position) {
		Iterator<Position> positionIterator = positions.iterator();

		while(positionIterator.hasNext() && position.isEmpty()) {
			Position p = positionIterator.next();

			if(!p.isFilled() && p.getTitle().equals(title)) {
				// it wasn't filled and the title matched
				// You're hired!
				Employee employee = new Employee(id++, person);
				p.setEmployee(Optional.of(employee));
				return Optional.of(p); // found
			}

			if(p.getDirectReports().size() != 0) {
				position = this.positionFinder(p.getDirectReports(), person, title, position);
			}
		}

		// return found position
		return position;
	}

	@Override
	public String toString() {
		return printOrganization(root, "");
	}
	
	private String printOrganization(Position pos, String prefix) {
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for(Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}
}
