/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.misc.HashcodeUtil;

public class BuildTarget {
	
	protected final boolean enabled;
	protected final String targetName;
	
	public BuildTarget(boolean enabled, String targetName) {
		this.enabled = enabled;
		this.targetName = targetName;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getTargetName() {
		return targetName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BuildTarget)) return false;
		
		BuildTarget other = (BuildTarget) obj;
		
		return 
				areEqual(enabled, other.enabled) &&
				areEqual(targetName, other.targetName);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(enabled, targetName);
	}
	
}