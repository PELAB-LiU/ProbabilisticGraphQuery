/*******************************************************************************
 * Copyright (c) 2010-2012, Istvan Rath and Daniel Varro
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-v20.html.
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.viatra.query.runtime.rete.index;

import java.lang.ref.WeakReference;

import org.eclipse.viatra.query.runtime.rete.network.Node;

public abstract class DefaultIndexerListener implements IndexerListener {

    WeakReference<Node> owner;

    public DefaultIndexerListener(Node owner) {
        this.owner = new WeakReference<Node>(owner);
    }

    @Override
    public Node getOwner() {
        return owner.get();
    }

}