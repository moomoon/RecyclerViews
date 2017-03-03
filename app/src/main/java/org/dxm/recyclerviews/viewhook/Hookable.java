package org.dxm.recyclerviews.viewhook;

import android.support.annotation.NonNull;

/**
 * Created by phoebe on 03/03/2017.
 */

public interface Hookable {
    void hookup(@NonNull ViewHook hook);
}
