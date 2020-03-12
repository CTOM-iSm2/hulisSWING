package util;

public class Version implements Comparable {
	public String comment;
	public int corr;
	public int maj;
	public int min;

	public Version(int maj, int min, int corr) {
		this.maj = maj;
		this.min = min;
		this.corr = corr;
		this.comment = "";
	}

	public Version(int maj, int min, int corr, String comment) {
		this.maj = maj;
		this.min = min;
		this.corr = corr;
		this.comment = comment;

	}

	public int compareTo(Object o) {
		if (!(o instanceof Version))
			throw new ClassCastException();
		Version v = (Version) o;
		if (this.maj != v.maj)
			return new Integer(this.maj).compareTo(v.maj);
		else if (this.min != v.min)
			return new Integer(this.min).compareTo(v.min);
		else
			return new Integer(this.corr).compareTo(v.corr);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Version))
			return false;
		Version v = (Version) o;
		if ((this.maj == v.maj) && (this.min == v.min) && (this.corr == v.corr))
			return true;
		else
			return false;

	}

	@Override
	public String toString() {
		String res = "";
		res = this.maj + "." + this.min;
		if (this.corr != 0)
			res += "." + this.corr;
		if (this.comment != "")
			res += " " + this.comment;
		return res;

	}

}
