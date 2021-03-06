/*
 * Copyright (c) 2015 NetIDE Consortium and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netide.openflowjava.protocol.impl.deserialization.factories;

import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opendaylight.netide.openflowjava.protocol.impl.deserialization.NetIdeDeserializerRegistryImpl;
import org.opendaylight.netide.openflowjava.protocol.impl.util.BufferHelper;
import org.opendaylight.openflowjava.protocol.api.extensibility.DeserializerRegistry;
import org.opendaylight.openflowjava.protocol.api.keys.MessageCodeKey;
import org.opendaylight.openflowjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MultipartRequestFlags;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.MultipartRequestInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.MultipartRequestBody;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.MultipartRequestPortStatsCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.multipart.request.port.stats._case.MultipartRequestPortStatsBuilder;

/**
 * @author giuseppex.petralia@intel.com
 *
 */
public class OF10StatsRequestInputPortStatsFactoryTest {
    ByteBuf bb = BufferHelper.buildBuffer("00 04 00 00 00 0f 00 00 00 00 00 00");

    MultipartRequestInput deserializedMessage;

    @Before
    public void startUp() throws Exception {
        DeserializerRegistry desRegistry = new NetIdeDeserializerRegistryImpl();
        desRegistry.init();
        OF10StatsRequestInputFactory factory = desRegistry
                .getDeserializer(new MessageCodeKey(EncodeConstants.OF10_VERSION_ID, 16, MultipartRequestInput.class));

        deserializedMessage = BufferHelper.deserialize(factory, bb);
    }

    @Test
    public void test() throws Exception {
        BufferHelper.checkHeaderV10(deserializedMessage);

        Assert.assertEquals("Wrong type", 4, deserializedMessage.getType().getIntValue());
        Assert.assertEquals("Wrong flags", new MultipartRequestFlags(false), deserializedMessage.getFlags());
        Assert.assertEquals("Wrong body", createMultipartRequestBody(), deserializedMessage.getMultipartRequestBody());
    }

    private static MultipartRequestBody createMultipartRequestBody() {
        MultipartRequestPortStatsCaseBuilder caseBuilder = new MultipartRequestPortStatsCaseBuilder();
        MultipartRequestPortStatsBuilder portBuilder = new MultipartRequestPortStatsBuilder();
        portBuilder.setPortNo(15L);
        caseBuilder.setMultipartRequestPortStats(portBuilder.build());
        return caseBuilder.build();
    }
}
