package dailyproject.moon.IO.netty.getty.socket;//package com.yuxing.project.util.socket;
//
//import com.gettyio.core.channel.AioChannel;
//import com.gettyio.core.handler.codec.string.StringDecoder;
//import com.gettyio.core.util.LinkedNonBlockQueue;
//
//import java.nio.ByteBuffer;
//
//public class ServerDecode extends StringDecoder {
//
//    @Override
//    public void decode(AioChannel aioChannel, ByteBuffer obj, LinkedNonBlockQueue<Object> out) throws Exception {
//        String HEXES = "0123456789ABCDEF";
//
//        byte[] req =new byte[1024];
//        obj.g
//        final StringBuilder hex = new StringBuilder(2 * req.length);
//
//        for (int i = 0; i < req.length; i++) {
//            byte b = req[i];
//            hex.append(HEXES.charAt((b & 0xF0) >> 4))
//                    .append(HEXES.charAt((b & 0x0F)));
//        }
//        out.put(hex.toString());
//    }
//}
