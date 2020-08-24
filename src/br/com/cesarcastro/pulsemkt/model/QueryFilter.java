package br.com.cesarcastro.pulsemkt.model;

import javax.validation.constraints.NotNull;

import br.com.cesarcastro.pulsemkt.enums.Comparator;

public class QueryFilter {

	@NotNull
	private String field;
	@NotNull
	private Comparator comparator;
	@NotNull
	private String[] values;

	public QueryFilter(String field, Comparator comparator, String... values) {
		this.field = field;
		this.comparator = comparator;
		this.values = values;
	}

	public String getFilter() {
		String filter;
		switch(comparator){
		case IN:
			filter = String.format(" %s %s (%s) ", this.field, this.comparator, values.toString().replace("[", "").replace("]",""));
			break;
		case LIKE:
			filter = String.format(" %s %s '%s' ", this.field, this.comparator, "%"+values[0]+"%");
			break;
		default:
			filter = String.format(" %s %s %s ", this.field, this.comparator, values[0]);
			break;
		}
		return filter;
	}
}
