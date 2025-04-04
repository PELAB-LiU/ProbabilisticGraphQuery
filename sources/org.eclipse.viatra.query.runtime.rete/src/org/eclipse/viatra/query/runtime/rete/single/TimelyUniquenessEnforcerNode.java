/*******************************************************************************
 * Copyright (c) 2010-2019, Tamas Szabo, Istvan Rath and Daniel Varro
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-v20.html.
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.viatra.query.runtime.rete.single;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.viatra.query.runtime.matchers.tuple.Tuple;
import org.eclipse.viatra.query.runtime.matchers.util.Direction;
import org.eclipse.viatra.query.runtime.matchers.util.Signed;
import org.eclipse.viatra.query.runtime.matchers.util.TimelyMemory;
import org.eclipse.viatra.query.runtime.matchers.util.timeline.Diff;
import org.eclipse.viatra.query.runtime.matchers.util.timeline.Timeline;
import org.eclipse.viatra.query.runtime.rete.index.ProjectionIndexer;
import org.eclipse.viatra.query.runtime.rete.index.timely.TimelyMemoryIdentityIndexer;
import org.eclipse.viatra.query.runtime.rete.index.timely.TimelyMemoryNullIndexer;
import org.eclipse.viatra.query.runtime.rete.matcher.TimelyConfiguration.TimelineRepresentation;
import org.eclipse.viatra.query.runtime.rete.network.ReteContainer;
import org.eclipse.viatra.query.runtime.rete.network.communication.CommunicationGroup;
import org.eclipse.viatra.query.runtime.rete.network.communication.Timestamp;
import org.eclipse.viatra.query.runtime.rete.network.communication.timely.ResumableNode;
import org.eclipse.viatra.query.runtime.rete.network.mailbox.Mailbox;
import org.eclipse.viatra.query.runtime.rete.network.mailbox.timely.TimelyMailbox;

/**
 * Timely uniqueness enforcer node implementation.
 * 
 * @author Tamas Szabo
 * @noinstantiate This class is not intended to be instantiated by clients.
 * @noextend This class is not intended to be subclassed by clients.
 * @since 2.4
 */
public class TimelyUniquenessEnforcerNode extends AbstractUniquenessEnforcerNode implements ResumableNode {

    protected final TimelyMemory<Timestamp> memory;
    /**
     * @since 2.4
     */
    protected CommunicationGroup group;

    public TimelyUniquenessEnforcerNode(final ReteContainer container, final int tupleWidth) {
        super(container, tupleWidth);
        this.memory = new TimelyMemory<Timestamp>(
                container.getTimelyConfiguration().getTimelineRepresentation() == TimelineRepresentation.FAITHFUL);
        container.registerClearable(this.memory);
        this.mailbox = instantiateMailbox();
        container.registerClearable(this.mailbox);
    }

    protected Mailbox instantiateMailbox() {
        return new TimelyMailbox(this, this.reteContainer);
    }

    @Override
    public void pullInto(final Collection<Tuple> collector, final boolean flush) {
        for (final Tuple tuple : this.memory.getTuplesAtInfinity()) {
            collector.add(tuple);
        }
    }

    @Override
    public CommunicationGroup getCurrentGroup() {
        return this.group;
    }

    @Override
    public void setCurrentGroup(final CommunicationGroup group) {
        this.group = group;
    }

    @Override
    public Set<Tuple> getTuples() {
        return this.memory.getTuplesAtInfinity();
    }

    /**
     * @since 2.4
     */
    @Override
    public Timestamp getResumableTimestamp() {
        return this.memory.getResumableTimestamp();
    }

    /**
     * @since 2.4
     */
    @Override
    public void resumeAt(final Timestamp timestamp) {
        final Map<Tuple, Diff<Timestamp>> diffMap = this.memory.resumeAt(timestamp);
        for (final Entry<Tuple, Diff<Timestamp>> entry : diffMap.entrySet()) {
            for (final Signed<Timestamp> signed : entry.getValue()) {
                propagate(signed.getDirection(), entry.getKey(), signed.getPayload());
            }
        }
        final Timestamp nextTimestamp = this.memory.getResumableTimestamp();
        if (nextTimestamp != null) {
            this.group.notifyHasMessage(this.mailbox, nextTimestamp);
        }
    }

    @Override
    public void update(final Direction direction, final Tuple update, final Timestamp timestamp) {
        Diff<Timestamp> resultDiff = null;
        if (direction == Direction.INSERT) {
            resultDiff = this.memory.put(update, timestamp);
        } else {
            try {
                resultDiff = this.memory.remove(update, timestamp);
            } catch (final IllegalStateException e) {
                issueError("[INTERNAL ERROR] Duplicate deletion of " + update + " was detected in "
                        + this.getClass().getName() + " " + this + " for pattern(s) "
                        + getTraceInfoPatternsEnumerated(), e);
                // diff will remain unset in case of the exception, it is time to return
                return;
            }
        }

        for (final Signed<Timestamp> signed : resultDiff) {
            propagate(signed.getDirection(), update, signed.getPayload());
        }
    }

    @Override
    public void pullIntoWithTimeline(final Map<Tuple, Timeline<Timestamp>> collector, final boolean flush) {
        collector.putAll(this.memory.asMap());
    }

    @Override
    public ProjectionIndexer getNullIndexer() {
        if (this.memoryNullIndexer == null) {
            this.memoryNullIndexer = new TimelyMemoryNullIndexer(this.reteContainer, this.tupleWidth, this.memory, this,
                    this, this.specializedListeners);
            this.getCommunicationTracker().registerDependency(this, this.memoryNullIndexer);
        }
        return this.memoryNullIndexer;
    }

    @Override
    public ProjectionIndexer getIdentityIndexer() {
        if (this.memoryIdentityIndexer == null) {
            this.memoryIdentityIndexer = new TimelyMemoryIdentityIndexer(this.reteContainer, this.tupleWidth,
                    this.memory, this, this, this.specializedListeners);
            this.getCommunicationTracker().registerDependency(this, this.memoryIdentityIndexer);
        }
        return this.memoryIdentityIndexer;
    }

    @Override
    public void networkStructureChanged() {
        super.networkStructureChanged();
    }

}