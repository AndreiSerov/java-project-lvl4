package hexlet.code.domain.finder;

import hexlet.code.domain.Url;
import hexlet.code.domain.query.QUrl;
import hexlet.code.domain.query.QUrlCheck;
import io.ebean.Finder;
import io.ebean.PagedList;
import org.jetbrains.annotations.NotNull;

/**
 * @author andreiserov
 */
public final class UrlFinder extends Finder<Long, Url> {
    private final QUrlCheck check = QUrlCheck.alias();

    public PagedList<Url> getPage(int pageNumber) {
        return new QUrl()
            .checks.fetch(check.createdAt, check.statusCode)
            .orderBy()
                .id.asc()
                .checks.id.desc()
            .setFirstRow(pageNumber * 12) // 10 being 1 * maxRows
            .setMaxRows(12)
            .findPagedList();
    }

    public Url byId(@NotNull Long id) {
        return new QUrl()
            .id.eq(id)
            .checks.fetch()
            .orderBy().checks.id.desc()
            .findOne();
    }

    public Url byName(String name) {
        return query().where().eq("name", name).findOne();
    }

    public UrlFinder() {
        super(Url.class);
    }
}
