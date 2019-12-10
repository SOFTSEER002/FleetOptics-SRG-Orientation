package com.doozycod.fleetoptics.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerConfigModel {

    public ServerConfigModel.server_config getServer_config() {
        return server_config;
    }

    public void setServer_config(ServerConfigModel.server_config server_config) {
        this.server_config = server_config;
    }

    @SerializedName("server_config")
    @Expose
    private ServerConfigModel.server_config server_config;

    /*private List<server_config> server_config = null;

    public List<server_config> getServerConfigList() {
        return server_config;
    }

    public void setServerConfigList(List<server_config> serverConfigList) {
        this.server_config = serverConfigList;
    }*/

    public class server_config {

        @SerializedName("id")
        @Expose
        String id;
        @SerializedName("server_name")
        @Expose
        String server_name;
        @SerializedName("username")
        @Expose
        String username;
        @SerializedName("password")
        @Expose
        String password;
        @SerializedName("server_address")
        @Expose
        String server_address;
        @SerializedName("log_level")
        @Expose
        String log_level;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getServer_name() {
            return server_name;
        }

        public void setServer_name(String server_name) {
            this.server_name = server_name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getServer_address() {
            return server_address;
        }

        public void setServer_address(String server_address) {
            this.server_address = server_address;
        }

        public String getLog_level() {
            return log_level;
        }

        public void setLog_level(String log_level) {
            this.log_level = log_level;
        }
    }
}
