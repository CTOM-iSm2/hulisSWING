package org.ctom.hulis.huckel;


import java.io.Serializable;
import java.util.ArrayList;

import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.exception.MonoExcitationException;
import org.ctom.hulis.huckel.listeners.IMonoExcitationListener;
import org.ctom.hulis.huckel.structures.Structure;



public class MonoExcitation implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -922625160616152374L;
	
	SpinOrbitaleMoleculaire from;
	SpinOrbitaleMoleculaire to;
	private transient final ArrayList<IMonoExcitationListener> listeners;
	protected transient Structure structure;

	public Structure getStructure(){
		return structure;
		
	}
	
	public MonoExcitation(Structure struct) {
		super();
		this.structure = struct;
		this.listeners = new ArrayList<IMonoExcitationListener>();
		addMonoExcitationListener(struct);
	}

	private MonoExcitation(SpinOrbitaleMoleculaire from, SpinOrbitaleMoleculaire to, Structure structure,
			ArrayList<IMonoExcitationListener> listeners) {
		super();
		this.from = from;
		this.to = to;
		this.structure = structure;
		this.listeners = listeners;
	}

	public void addMonoExcitationListener(IMonoExcitationListener listener) {
		this.listeners.add(listener);
	}

	public void check()  {
		if (this.to == null)
			return;
		if (this.from == null)
			return;
		triggerMonoExcitation(this, this.from, this.to);
		// NG : mise en commentaire des deux lignes suivantes car on a besoin de se referer aux spins OM apres le trigger (dans FrameExcitation), donc ne pas les remettre a null
		//this.from = null;
		//this.to = null;
	}

	@Override
	public Object clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}

		return new MonoExcitation(this.from, this.to, this.structure,
				this.listeners);
	}

	public SpinOrbitaleMoleculaire getFrom() {
		return this.from;
	}

	public SpinOrbitaleMoleculaire getTo() {
		return this.to;
	}

	public void removeMonoExcitationListener(IMonoExcitationListener listener) {
		this.listeners.remove(listener);
	}

	public void setFrom(SpinOrbitaleMoleculaire som) throws MonoExcitationException {
		if ((som.getSpin() == Spin.NOSPIN_ALPHA)||(som.getSpin() == Spin.NOSPIN_BETA))
			throw new MonoExcitationException(
					"Cannot get an electron from an already empty orbital");
		this.from = som;
	}

	public void setTo(SpinOrbitaleMoleculaire sOM) throws MonoExcitationException {
		if ((sOM.getSpin() == Spin.ALPHA)||(sOM.getSpin() == Spin.BETA))
			throw new MonoExcitationException("Cannot fill an already filled orbital");
		this.to = sOM;
	}

	protected void triggerMonoExcitation(MonoExcitation source, Object oldValue, Object newValue) {

		MonoExcitationEvent event = null;

		for (IMonoExcitationListener listener : this.getListeners() ) {
			if (event == null)
				event = new MonoExcitationEvent(source, oldValue, newValue);
			listener.monoExcitationTriggered(event);
		}
	}
	
	protected ArrayList<IMonoExcitationListener> getListeners() {
		return (ArrayList<IMonoExcitationListener> ) this.listeners.clone();
	}

}
