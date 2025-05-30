package attus.proc.proc_jur.service.filter;

import attus.proc.proc_jur.dto.ProcessFilter;

public class ProcessFilterSelector {

    public static FilterStrategy select(final ProcessFilter filter) {
        if (filter.getStatus() != null) {
            return new StatusFilterStrategy();
        } else if (filter.getOpeningDate() != null) {
            return new OpeningDateFilterStrategy();
        } else if (filter.getLegalEntityId() != null) {
            return new LegalEntityIdFilterStrategy();
        }
        throw new IllegalArgumentException("No valid filter found, Selector requires STATUS, OPENING DATE or LEGAL ENTITY ID");
    }
}
