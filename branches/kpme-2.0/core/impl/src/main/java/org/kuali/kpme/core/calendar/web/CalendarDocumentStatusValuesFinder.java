package org.kuali.kpme.core.calendar.web;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentStatusCategory;
import org.kuali.rice.kew.impl.document.search.DocumentStatusValuesFinder;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CalendarDocumentStatusValuesFinder extends KeyValuesBase {
    private static final String CATEGORY_CODE_PREFIX = "category:";

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> statuses = new ArrayList<KeyValue>();
        statuses.add(new ConcreteKeyValue(StringUtils.EMPTY, StringUtils.EMPTY));
        addCategory(statuses, DocumentStatusCategory.PENDING);
        addCategory(statuses, DocumentStatusCategory.SUCCESSFUL);
        addCategory(statuses, DocumentStatusCategory.UNSUCCESSFUL);
        return statuses;
    }

    private void addCategory(List<KeyValue> statuses, DocumentStatusCategory category) {
        statuses.add(new ConcreteKeyValue(CATEGORY_CODE_PREFIX + category.getCode(), category.getLabel() + " Statuses"));
        Set<DocumentStatus> documentStatuses = DocumentStatus.getStatusesForCategory(category);
        for (DocumentStatus documentStatus : documentStatuses) {
            statuses.add(new ConcreteKeyValue(documentStatus.getCode(), "- " + documentStatus.getLabel()));
        }
    }
}
