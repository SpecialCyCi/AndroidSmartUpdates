package com.special.AndroidSmartUpdates.impl;

import com.special.AndroidSmartUpdates.model.PatchInformation;

/**
 * User: special
 * Date: 13-10-4
 * Time: 下午3:25
 * Mail: specialcyci@gmail.com
 */
public interface UpdateListener {

    void hasUpdate(PatchInformation information);

    void hasNoUpdate();

}
