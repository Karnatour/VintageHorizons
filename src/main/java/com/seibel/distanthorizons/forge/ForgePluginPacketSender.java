package com.seibel.distanthorizons.forge;

import com.seibel.distanthorizons.common.AbstractPluginPacketSender;
import com.seibel.distanthorizons.common.wrappers.misc.ServerPlayerWrapper;
import com.seibel.distanthorizons.core.network.messages.AbstractNetworkMessage;
import com.seibel.distanthorizons.core.network.messages.MessageRegistry;
import com.seibel.distanthorizons.core.wrapperInterfaces.misc.IServerPlayerWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ForgePluginPacketSender extends AbstractPluginPacketSender
{
	public static final SimpleNetworkWrapper PLUGIN_CHANNEL =
			NetworkRegistry.INSTANCE.newSimpleChannel(
					AbstractPluginPacketSender.WRAPPER_PACKET_RESOURCE
			);

	public static void setPacketHandler(Consumer<AbstractNetworkMessage> consumer)
	{
		setPacketHandler((player, message) -> consumer.accept(message));
	}
    static BiConsumer<IServerPlayerWrapper, AbstractNetworkMessage> consumerPacket;
	public static void setPacketHandler(BiConsumer<IServerPlayerWrapper, AbstractNetworkMessage> consumer)
	{
        PLUGIN_CHANNEL.registerMessage(MessageWrapper.Handler.class, MessageWrapper.class, 0, Side.CLIENT);
        PLUGIN_CHANNEL.registerMessage(MessageWrapper.Handler.class, MessageWrapper.class, 0, Side.SERVER);
        consumerPacket = consumer;
	}

	@Override
	public void sendToServer(AbstractNetworkMessage message)
	{
        PLUGIN_CHANNEL.sendToServer(new MessageWrapper(message));
	}

	@Override
	public void sendToClient(EntityPlayerMP serverPlayer, AbstractNetworkMessage message)
	{
		PLUGIN_CHANNEL.sendTo(new MessageWrapper(message), serverPlayer);
	}

	// Forge doesn't support using abstract classes
	@SuppressWarnings({"ClassCanBeRecord", "RedundantSuppression"})
	public static class MessageWrapper implements IMessage
	{
		public AbstractNetworkMessage message;

		public MessageWrapper(AbstractNetworkMessage message) { this.message = message; }

        public MessageWrapper()
        {
            // For reflection
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            int messageId = buf.readByte();
            message = MessageRegistry.INSTANCE.createMessage(messageId);
            message.decode(buf);
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeByte(MessageRegistry.INSTANCE.getMessageId(message));
            message.encode(buf);
        }

        public static class Handler implements IMessageHandler<MessageWrapper, IMessage>
        {
            @Override
            public IMessage onMessage(MessageWrapper wrapper, MessageContext context) {
                if (wrapper.message != null)
                {
                    if (context.side == Side.SERVER)
                    {
                        consumerPacket.accept(ServerPlayerWrapper.getWrapper(context.getServerHandler().player), wrapper.message);
                    }
                    else {
                        consumerPacket.accept(null, wrapper.message);
                    }
                }
                return null; // No response needed
            }
        }
    }

}
