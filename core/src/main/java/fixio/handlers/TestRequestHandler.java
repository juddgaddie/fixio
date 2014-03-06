/*
 * Copyright 2014 The FIX.io Project
 *
 * The FIX.io Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package fixio.handlers;

import fixio.fixprotocol.FixMessage;
import fixio.fixprotocol.FixMessageBuilder;
import fixio.fixprotocol.FixMessageBuilderImpl;
import fixio.fixprotocol.MessageTypes;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

import static fixio.fixprotocol.FieldType.TestReqID;

/**
 * Handles FIX TestRequest messages and responds with Heartbeat message.
 *
 * @author Konstantin Pavlov
 */
@ChannelHandler.Sharable
public class TestRequestHandler implements IFixMsgHandler {

    @Override
    public boolean isProcess(ChannelHandlerContext ctx, FixMessage msg) {
        if (MessageTypes.TEST_REQUEST.equals(msg.getMessageType())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isProcess(ChannelHandlerContext ctx, FixMessageBuilder msg) {
        return false;
    }

    @Override
    public void process(ChannelHandlerContext ctx, FixMessage msg) {
        final String testReqId = msg.getString(TestReqID);
        final FixMessageBuilderImpl builder = new FixMessageBuilderImpl(MessageTypes.HEARTBEAT);
        builder.add(TestReqID, testReqId);

        ctx.channel().writeAndFlush(builder);
    }

    @Override
    public void beforeSendMessage(ChannelHandlerContext ctx, FixMessageBuilder messageBuilder) {
    }
}
