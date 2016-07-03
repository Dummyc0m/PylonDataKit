package com.dummyc0m.pylon.datakit.network

import com.dummyc0m.pylon.datakit.network.message.Message
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelId
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by Dummy on 6/13/16.
 */
class MessageManager {
    internal val channels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    private val serverIdMap: MutableMap<String, ChannelId> = ConcurrentHashMap()
    private val handlerMap: MutableMap<Int, MessageHandler<Message>> = ConcurrentHashMap()
    private val messageIdMap: MutableMap<Int, Class<out Message>> = ConcurrentHashMap()
    private val idMessageMap: MutableMap<Class<out Message>, Int> = ConcurrentHashMap()
    private var messageId = 0
    private val mapper = ObjectMapper()
    private var lastWriteFuture: ChannelFuture? = null

    internal fun <T: Message> registerHandler(messageClass: Class<T>, handler: MessageHandler<T>) {
        messageIdMap.put(messageId, messageClass)
        idMessageMap.put(messageClass, messageId)
        handlerMap.put(messageId++, handler as MessageHandler<Message>)
    }

    internal fun broadcast(message: Message) {
        val node = mapper.createObjectNode()
        node.put("id", idMessageMap.get(message.javaClass))
                .set("msg", message.toJson())
        lastWriteFuture = channels.writeAndFlush(node.toString()).first()
    }

    internal fun send(message: Message, channel: Channel) {
        val node = mapper.createObjectNode()
        node.put("id", idMessageMap.get(message.javaClass))
                .set("msg", message.toJson())
        lastWriteFuture = channel.writeAndFlush(node.toString())
    }

    internal fun send(message: Message, serverId: String) {
        //assume good input
        send(message, channels.find(serverIdMap.get(serverId)!!))
    }

    internal fun handle(ctx: ChannelHandlerContext, json: String) {
        try {
            val jsonNode = mapper.readTree(json)
            val messageId = jsonNode.get("id").intValue()
            val message = messageIdMap.get(messageId)!!.newInstance()
            message.fromJson(jsonNode.get("msg"))
            handlerMap.get(messageId)!!.handle(ctx, message)
        } catch (exception: Exception) {
            throw RuntimeException("Incompatible MessageType from " + ctx.name(), exception)
        }
    }

    internal fun contains(serverId: String): Boolean {
        return serverIdMap.contains(serverId)
    }

    internal fun put(serverId: String, channel: Channel) {
        serverIdMap.put(serverId, channel.id())
        channels.add(channel)
    }

    internal fun remove(channel: Channel) {
        serverIdMap.values.remove(channel.id())
        channels.remove(channel)
    }

    internal fun shutdown() {
        lastWriteFuture?.sync()
    }
}