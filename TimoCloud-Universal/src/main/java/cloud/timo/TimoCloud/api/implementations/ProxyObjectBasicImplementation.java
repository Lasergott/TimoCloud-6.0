package cloud.timo.TimoCloud.api.implementations;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.async.APIRequest;
import cloud.timo.TimoCloud.api.async.APIRequestFuture;
import cloud.timo.TimoCloud.api.messages.objects.AddressedPluginMessage;
import cloud.timo.TimoCloud.api.messages.objects.MessageClientAddress;
import cloud.timo.TimoCloud.api.messages.objects.MessageClientAddressType;
import cloud.timo.TimoCloud.api.messages.objects.PluginMessage;
import cloud.timo.TimoCloud.api.objects.BaseObject;
import cloud.timo.TimoCloud.api.objects.PlayerObject;
import cloud.timo.TimoCloud.api.objects.ProxyGroupObject;
import cloud.timo.TimoCloud.api.objects.ProxyObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

import static cloud.timo.TimoCloud.api.async.APIRequestType.P_EXECUTE_COMMAND;
import static cloud.timo.TimoCloud.api.async.APIRequestType.P_STOP;

@JsonIgnoreProperties({"messageClientAddress"})
public class ProxyObjectBasicImplementation implements ProxyObject, Comparable {

    private String name;
    private String id;
    private String group;
    private List<PlayerObject> onlinePlayers;
    private int onlinePlayerCount;
    private String base;
    private InetSocketAddress inetSocketAddress;
    private MessageClientAddress messageClientAddress;

    public ProxyObjectBasicImplementation() {}

    public ProxyObjectBasicImplementation(String name, String id, String group, List<PlayerObject> onlinePlayers, int onlinePlayerCount, String base, InetSocketAddress inetSocketAddress) {
        this.name = name;
        this.id = id;
        this.group = group;
        this.onlinePlayers = onlinePlayers;
        this.onlinePlayerCount = onlinePlayerCount;
        this.base = base;
        this.inetSocketAddress = inetSocketAddress;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ProxyGroupObject getGroup() {
        return TimoCloudAPI.getUniversalAPI().getProxyGroup(getGroupName());
    }

    public String getGroupName() {
        return group;
    }

    @Override
    public List<PlayerObject> getOnlinePlayers() {
        return onlinePlayers;
    }

    @Override
    public int getOnlinePlayerCount() {
        return onlinePlayerCount;
    }

    @Override
    public BaseObject getBase() {
        return TimoCloudAPI.getUniversalAPI().getBase(base);
    }

    @Override
    public InetSocketAddress getSocketAddress() {
        return inetSocketAddress;
    }

    @Override
    public InetAddress getIpAddress() {
        return inetSocketAddress.getAddress();
    }

    @Override
    public int getPort() {
        return inetSocketAddress.getPort();
    }

    @Override
    public MessageClientAddress getMessageAddress() {
        if (messageClientAddress == null) messageClientAddress = new MessageClientAddress(getId(), MessageClientAddressType.PROXY);
        return messageClientAddress;
    }

    @Override
    public APIRequestFuture executeCommand(String command) {
        return new APIRequest(P_EXECUTE_COMMAND, getName(), command).submit();

    }

    @Override
    public APIRequestFuture stop() {
        return new APIRequest(P_STOP, getName(), null).submit();
    }

    @Override
    public void sendPluginMessage(PluginMessage message) {
        TimoCloudAPI.getMessageAPI().sendMessage(new AddressedPluginMessage(getMessageAddress(), message));
    }

    @Override
    public int compareTo(Object o) {
        if (! (o instanceof ProxyObject)) return 1;
        ProxyObject so = (ProxyObject) o;
        try {
            return Integer.parseInt(getName().split("-")[getName().split("-").length-1]) - Integer.parseInt(so.getName().split("-")[so.getName().split("-").length-1]);
        } catch (Exception e) {
            return getName().compareTo(so.getName());
        }
    }
}
