package hexlet.code.domain.finder;

import hexlet.code.domain.Url;
import io.ebean.Finder;
import io.ebean.PagedList;

import java.util.UUID;

/**
 * @author andreiserov
 */
public final class UrlFinder extends Finder<UUID, Url> {

    public PagedList<Url> getPage(int pageNumber) {
        return query()
            .order("created_at")
            .setFirstRow(pageNumber * 12 + 1) // 10 being 1 * maxRows
            .setMaxRows(12)
            .findPagedList();
    }

    public UrlFinder() {
        super(Url.class);
    }
}
