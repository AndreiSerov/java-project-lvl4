package hexlet.code.domain.finder;

import hexlet.code.domain.Url;
import io.ebean.Finder;

import java.util.UUID;

/**
 * @author andreiserov
 */
public class UrlFinder extends Finder<UUID, Url> {

    public UrlFinder() {
        super(Url.class);
    }
}
