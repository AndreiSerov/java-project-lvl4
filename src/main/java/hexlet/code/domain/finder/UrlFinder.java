package hexlet.code.domain.finder;

import hexlet.code.domain.Url;
import io.ebean.Finder;
import io.ebean.PagedList;

/**
 * @author andreiserov
 */
public final class UrlFinder extends Finder<Long, Url> {

    public PagedList<Url> getPage(int pageNumber) {
        return query()
            .order("created_at")
            .setFirstRow(pageNumber * 12) // 10 being 1 * maxRows
            .setMaxRows(12)
            .findPagedList();
    }

    public UrlFinder() {
        super(Url.class);
    }
}
