/**
 * wesoft.com Inc.
 * Copyright (c) 2005-2016 All Rights Reserved.
 */
package blue.toolbox.communication;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author <a href="mailto:chencq@hzwesoft.com">chencq</a>
 * @version $Id: NettyDataSender.java, 2016/11/30 15:22 $
 */
public class NettyDataSender {

    private static SocketConnector connector;

    public static void send(String host, int port, byte[] data) {
        connector = new SocketConnector(host, port);
        connector.write(data);
    }

}

class SocketConnector {

    public static EventLoopGroup eventLoop = new NioEventLoopGroup(10);

    private String               host;

    private int                  port;

    public SocketConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void write(final byte[] bytes) {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoop).channel(NioSocketChannel.class).remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new SocketClientChannelHandler(bytes));
            }

        });

        try {
            bootstrap.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ChannelHandler.Sharable
    private class SocketClientChannelHandler extends ChannelInboundHandlerAdapter {

        private byte[] bytes;

        public SocketClientChannelHandler(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(Unpooled.copiedBuffer(bytes));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }

    }

}
